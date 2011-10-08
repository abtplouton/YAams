#==============================================================================
# ** Interpreter (part 1)
#------------------------------------------------------------------------------
#  This interpreter runs event commands. This class is used within the
#  Game_System class and the Game_Event class.
#==============================================================================

class Interpreter
  #--------------------------------------------------------------------------
  # * Button Input
  #--------------------------------------------------------------------------
  def input_button
    # Determine pressed button
    n = 0
    for i in 1..18
      if Input.trigger?(i)
        n = i
      end
    end
    # If button was pressed
    if n > 0
      # Change value of variables
      $game_variables[@button_input_variable_id] = n
      $game_map.need_refresh = true
      # End button input
      @button_input_variable_id = 0
    end
  end
  #--------------------------------------------------------------------------
  # * Setup Choices
  #--------------------------------------------------------------------------
  def setup_choices(parameters)
    # Set choice item count to choice_max
    $game_temp.choice_max = parameters[0].size
    # Set choice to message_text
    for text in parameters[0]
      $game_temp.message_text += text + "\n"
    end
    # Set cancel processing
    $game_temp.choice_cancel_type = parameters[1]
    # Set callback
    current_indent = @list[@index].indent
    $game_temp.choice_proc = Proc.new { |n| @branch[current_indent] = n }
  end
  #--------------------------------------------------------------------------
  # * Actor Iterator (consider all party members)
  #     parameter : if 1 or more, ID; if 0, all
  #--------------------------------------------------------------------------
  def iterate_actor(parameter)
    # If entire party
    if parameter == 0
      # Loop for entire party
      for actor in $game_party.actors
        # Evaluate block
        yield actor
      end
    # If single actor
    else
      # Get actor
      actor = $game_actors[parameter]
      # Evaluate block
      yield actor if actor != nil
    end
  end
  #--------------------------------------------------------------------------
  # * Enemy Iterator (consider all troop members)
  #     parameter : If 0 or above, index; if -1, all
  #--------------------------------------------------------------------------
  def iterate_enemy(parameter)
    # If entire troop
    if parameter == -1
      # Loop for entire troop
      for enemy in $game_troop.enemies
        # Evaluate block
        yield enemy
      end
    # If single enemy
    else
      # Get enemy
      enemy = $game_troop.enemies[parameter]
      # Evaluate block
      yield enemy if enemy != nil
    end
  end
  #--------------------------------------------------------------------------
  # * Battler Iterator (consider entire troop and entire party)
  #     parameter1 : If 0, enemy; if 1, actor
  #     parameter2 : If 0 or above, index; if -1, all
  #--------------------------------------------------------------------------
  def iterate_battler(parameter1, parameter2)
    # If enemy
    if parameter1 == 0
      # Call enemy iterator
      iterate_enemy(parameter2) do |enemy|
        yield enemy
      end
    # If actor
    else
      # If entire party
      if parameter2 == -1
        # Loop for entire party
        for actor in $game_party.actors
          # Evaluate block
          yield actor
        end
      # If single actor (N exposed)
      else
        # Get actor
        actor = $game_party.actors[parameter2]
        # Evaluate block
        yield actor if actor != nil
      end
    end
  end


  #--------------------------------------------------------------------------
  # * Get Character
  #     parameter : parameter
  #--------------------------------------------------------------------------
  def get_character(parameter)
    # Branch by parameter
    case parameter
    when -1  # player
      return $game_player
    when 0  # this event
      events = $game_map.events
      return events == nil ? nil : events[@event_id]
    else  # specific event
      events = $game_map.events
      return events == nil ? nil : events[parameter]
    end
  end
  #--------------------------------------------------------------------------
  # * Calculate Operated Value
  #     operation    : operation
  #     operand_type : operand type (0: invariable 1: variable)
  #     operand      : operand (number or variable ID)
  #--------------------------------------------------------------------------
  def operate_value(operation, operand_type, operand)
    # Get operand
    if operand_type == 0
      value = operand
    else
      value = $game_variables[operand]
    end
    # Reverse sign of integer if operation is [decrease]
    if operation == 1
      value = -value
    end
    # Return value
    return value
  end
end

#==============================================================================
# ** Interpreter (part 3)
#------------------------------------------------------------------------------
#  This interpreter runs event commands. This class is used within the
#  Game_System class and the Game_Event class.
#==============================================================================

class Interpreter
  #--------------------------------------------------------------------------
  # * Show Text
  #--------------------------------------------------------------------------
  def command_101
    # If other text has been set to message_text
    if $game_temp.message_text != nil
      # End
      return false
    end
    # Set message end waiting flag and callback
    @message_waiting = true
    $game_temp.message_proc = Proc.new { @message_waiting = false }
    # Set message text on first line
    $game_temp.message_text = @list[@index].parameters[0] + "\n"
    line_count = 1
    # Loop
    loop do
      # If next event command text is on the second line or after
      if @list[@index+1].code == 401
        # Add the second line or after to message_text
        $game_temp.message_text += @list[@index+1].parameters[0] + "\n"
        line_count += 1
      # If event command is not on the second line or after
      else
        # If next event command is show choices
        if @list[@index+1].code == 102
          # If choices fit on screen
          if @list[@index+1].parameters[0].size <= 4 - line_count
            # Advance index
            @index += 1
            # Choices setup
            $game_temp.choice_start = line_count
            setup_choices(@list[@index].parameters)
          end
        # If next event command is input number
        elsif @list[@index+1].code == 103
          # If number input window fits on screen
          if line_count < 4
            # Advance index
            @index += 1
            # Number input setup
            $game_temp.num_input_start = line_count
            $game_temp.num_input_variable_id = @list[@index].parameters[0]
            $game_temp.num_input_digits_max = @list[@index].parameters[1]
          end
        end
        # Continue
        return true
      end
      # Advance index
      @index += 1
    end
  end
  #--------------------------------------------------------------------------
  # * Show Choices
  #--------------------------------------------------------------------------
  def command_102
    # If text has been set to message_text
    if $game_temp.message_text != nil
      # End
      return false
    end
    # Set message end waiting flag and callback
    @message_waiting = true
    $game_temp.message_proc = Proc.new { @message_waiting = false }
    # Choices setup
    $game_temp.message_text = ""
    $game_temp.choice_start = 0
    setup_choices(@parameters)
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * When [**]
  #--------------------------------------------------------------------------
  def command_402
    # If fitting choices are selected
    if @branch[@list[@index].indent] == @parameters[0]
      # Delete branch data
      @branch.delete(@list[@index].indent)
      # Continue
      return true
    end
    # If it doesn't meet the condition: command skip
    return command_skip
  end
  #--------------------------------------------------------------------------
  # * When Cancel
  #--------------------------------------------------------------------------
  def command_403
    # If choices are cancelled
    if @branch[@list[@index].indent] == 4
      # Delete branch data
      @branch.delete(@list[@index].indent)
      # Continue
      return true
    end
    # If it doen't meet the condition: command skip
    return command_skip
  end
  #--------------------------------------------------------------------------
  # * Input Number
  #--------------------------------------------------------------------------
  def command_103
    # If text has been set to message_text
    if $game_temp.message_text != nil
      # End
      return false
    end
    # Set message end waiting flag and callback
    @message_waiting = true
    $game_temp.message_proc = Proc.new { @message_waiting = false }
    # Number input setup
    $game_temp.message_text = ""
    $game_temp.num_input_start = 0
    $game_temp.num_input_variable_id = @parameters[0]
    $game_temp.num_input_digits_max = @parameters[1]
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Change Text Options
  #--------------------------------------------------------------------------
  def command_104
    # If message is showing
    if $game_temp.message_window_showing
      # End
      return false
    end
    # Change each option
    $game_system.message_position = @parameters[0]
    $game_system.message_frame = @parameters[1]
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Button Input Processing
  #--------------------------------------------------------------------------
  def command_105
    # Set variable ID for button input
    @button_input_variable_id = @parameters[0]
    # Advance index
    @index += 1
    # End
    return false
  end
  #--------------------------------------------------------------------------
  # * Conditional Branch
  #--------------------------------------------------------------------------
  def command_111
    # Initialize local variable: result
    result = false
    case @parameters[0]
    when 0  # switch
      result = ($game_switches[@parameters[1]] == (@parameters[2] == 0))
    when 1  # variable
      value1 = $game_variables[@parameters[1]]
      if @parameters[2] == 0
        value2 = @parameters[3]
      else
        value2 = $game_variables[@parameters[3]]
      end
      case @parameters[4]
      when 0  # value1 is equal to value2
        result = (value1 == value2)
      when 1  # value1 is greater than or equal to value2
        result = (value1 >= value2)
      when 2  # value1 is less than or equal to value2
        result = (value1 <= value2)
      when 3  # value1 is greater than value2
        result = (value1 > value2)
      when 4  # value1 is less than value2
        result = (value1 < value2)
      when 5  # value1 is not equal to value2
        result = (value1 != value2)
      end
    when 2  # self switch
      if @event_id > 0
        key = [$game_map.map_id, @event_id, @parameters[1]]
        if @parameters[2] == 0
          result = ($game_self_switches[key] == true)
        else
          result = ($game_self_switches[key] != true)
        end
      end
    when 3  # timer
      if $game_system.timer_working
        sec = $game_system.timer / Graphics.frame_rate
        if @parameters[2] == 0
          result = (sec >= @parameters[1])
        else
          result = (sec <= @parameters[1])
        end
      end
    when 4  # actor
      actor = $game_actors[@parameters[1]]
      if actor != nil
        case @parameters[2]
        when 0  # in party
          result = ($game_party.actors.include?(actor))
        when 1  # name
          result = (actor.name == @parameters[3])
        when 2  # skill
          result = (actor.skill_learn?(@parameters[3]))
        when 3  # weapon
          result = (actor.weapon_id == @parameters[3])
        when 4  # armor
          result = (actor.armor1_id == @parameters[3] or
                    actor.armor2_id == @parameters[3] or
                    actor.armor3_id == @parameters[3] or
                    actor.armor4_id == @parameters[3])
        when 5  # state
          result = (actor.state?(@parameters[3]))
        end
      end
    when 5  # enemy
      enemy = $game_troop.enemies[@parameters[1]]
      if enemy != nil
        case @parameters[2]
        when 0  # appear
          result = (enemy.exist?)
        when 1  # state
          result = (enemy.state?(@parameters[3]))
        end
      end
    when 6  # character
      character = get_character(@parameters[1])
      if character != nil
        result = (character.direction == @parameters[2])
      end
    when 7  # gold
      if @parameters[2] == 0
        result = ($game_party.gold >= @parameters[1])
      else
        result = ($game_party.gold <= @parameters[1])
      end
    when 8  # item
      result = ($game_party.item_number(@parameters[1]) > 0)
    when 9  # weapon
      result = ($game_party.weapon_number(@parameters[1]) > 0)
    when 10  # armor
      result = ($game_party.armor_number(@parameters[1]) > 0)
    when 11  # button
      result = (Input.press?(@parameters[1]))
    when 12  # script
      result = eval(@parameters[1])
    end
    # Store determinant results in hash
    @branch[@list[@index].indent] = result
    # If determinant results are true
    if @branch[@list[@index].indent] == true
      # Delete branch data
      @branch.delete(@list[@index].indent)
      # Continue
      return true
    end
    # If it doesn't meet the conditions: command skip
    return command_skip
  end
  #--------------------------------------------------------------------------
  # * Else
  #--------------------------------------------------------------------------
  def command_411
    # If determinant results are false
    if @branch[@list[@index].indent] == false
      # Delete branch data
      @branch.delete(@list[@index].indent)
      # Continue
      return true
    end
    # If it doesn't meet the conditions: command skip
    return command_skip
  end
  #--------------------------------------------------------------------------
  # * Loop
  #--------------------------------------------------------------------------
  def command_112
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Repeat Above
  #--------------------------------------------------------------------------
  def command_413
    # Get indent
    indent = @list[@index].indent
    # Loop
    loop do
      # Return index
      @index -= 1
      # If this event command is the same level as indent
      if @list[@index].indent == indent
        # Continue
        return true
      end
    end
  end
  #--------------------------------------------------------------------------
  # * Break Loop
  #--------------------------------------------------------------------------
  def command_113
    # Get indent
    indent = @list[@index].indent
    # Copy index to temporary variables
    temp_index = @index
    # Loop
    loop do
      # Advance index
      temp_index += 1
      # If a fitting loop was not found
      if temp_index >= @list.size-1
        # Continue
        return true
      end
      # If this event command is [repeat above] and indent is shallow
      if @list[temp_index].code == 413 and @list[temp_index].indent < indent
        # Update index
        @index = temp_index
        # Continue
        return true
      end
    end
  end
  #--------------------------------------------------------------------------
  # * Control Switches
  #--------------------------------------------------------------------------
  def command_121
    # Loop for group control
    for i in @parameters[0] .. @parameters[1]
      # Change switch
      $game_switches[i] = (@parameters[2] == 0)
    end
    # Refresh map
    $game_map.need_refresh = true
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Control Variables
  #--------------------------------------------------------------------------
  def command_122
    # Initialize value
    value = 0
    # Branch with operand
    case @parameters[3]
    when 0  # invariable
      value = @parameters[4]
    when 1  # variable
      value = $game_variables[@parameters[4]]
    when 2  # random number
      value = @parameters[4] + rand(@parameters[5] - @parameters[4] + 1)
    when 3  # item
      value = $game_party.item_number(@parameters[4])
    when 4  # actor
      actor = $game_actors[@parameters[4]]
      if actor != nil
        case @parameters[5]
        when 0  # level
          value = actor.level
        when 1  # EXP
          value = actor.exp
        when 2  # HP
          value = actor.hp
        when 3  # SP
          value = actor.sp
        when 4  # MaxHP
          value = actor.maxhp
        when 5  # MaxSP
          value = actor.maxsp
        when 6  # strength
          value = actor.str
        when 7  # dexterity
          value = actor.dex
        when 8  # agility
          value = actor.agi
        when 9  # intelligence
          value = actor.int
        when 10  # attack power
          value = actor.atk
        when 11  # physical defense
          value = actor.pdef
        when 12  # magic defense
          value = actor.mdef
        when 13  # evasion
          value = actor.eva
        end
      end
    when 5  # enemy
      enemy = $game_troop.enemies[@parameters[4]]
      if enemy != nil
        case @parameters[5]
        when 0  # HP
          value = enemy.hp
        when 1  # SP
          value = enemy.sp
        when 2  # MaxHP
          value = enemy.maxhp
        when 3  # MaxSP
          value = enemy.maxsp
        when 4  # strength
          value = enemy.str
        when 5  # dexterity
          value = enemy.dex
        when 6  # agility
          value = enemy.agi
        when 7  # intelligence
          value = enemy.int
        when 8  # attack power
          value = enemy.atk
        when 9  # physical defense
          value = enemy.pdef
        when 10  # magic defense
          value = enemy.mdef
        when 11  # evasion correction
          value = enemy.eva
        end
      end
    when 6  # character
      character = get_character(@parameters[4])
      if character != nil
        case @parameters[5]
        when 0  # x-coordinate
          value = character.x
        when 1  # y-coordinate
          value = character.y
        when 2  # direction
          value = character.direction
        when 3  # screen x-coordinate
          value = character.screen_x
        when 4  # screen y-coordinate
          value = character.screen_y
        when 5  # terrain tag
          value = character.terrain_tag
        end
      end
    when 7  # other
      case @parameters[4]
      when 0  # map ID
        value = $game_map.map_id
      when 1  # number of party members
        value = $game_party.actors.size
      when 2  # gold
        value = $game_party.gold
      when 3  # steps
        value = $game_party.steps
      when 4  # play time
        value = Graphics.frame_count / Graphics.frame_rate
      when 5  # timer
        value = $game_system.timer / Graphics.frame_rate
      when 6  # save count
        value = $game_system.save_count
      end
    end
    # Loop for group control
    for i in @parameters[0] .. @parameters[1]
      # Branch with control
      case @parameters[2]
      when 0  # substitute
        $game_variables[i] = value
      when 1  # add
        $game_variables[i] += value
      when 2  # subtract
        $game_variables[i] -= value
      when 3  # multiply
        $game_variables[i] *= value
      when 4  # divide
        if value != 0
          $game_variables[i] /= value
        end
      when 5  # remainder
        if value != 0
          $game_variables[i] %= value
        end
      end
      # Maximum limit check
      if $game_variables[i] > 99999999
        $game_variables[i] = 99999999
      end
      # Minimum limit check
      if $game_variables[i] < -99999999
        $game_variables[i] = -99999999
      end
    end
    # Refresh map
    $game_map.need_refresh = true
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Control Timer
  #--------------------------------------------------------------------------
  def command_124
    # If started
    if @parameters[0] == 0
      $game_system.timer = @parameters[1] * Graphics.frame_rate
      $game_system.timer_working = true
    end
    # If stopped
    if @parameters[0] == 1
      $game_system.timer_working = false
    end
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Change Party Member
  #--------------------------------------------------------------------------
  def command_129
    # Get actor
    actor = $game_actors[@parameters[0]]
    # If actor is valid
    if actor != nil
      # Branch with control
      if @parameters[1] == 0
        if @parameters[2] == 1
          $game_actors[@parameters[0]].setup(@parameters[0])
        end
        $game_party.add_actor(@parameters[0])
      else
        $game_party.remove_actor(@parameters[0])
      end
    end
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Transfer Player
  #--------------------------------------------------------------------------
  def command_201
    # If in battle
    if $game_temp.in_battle
      # Continue
      return true
    end
    # If transferring player, showing message, or processing transition
    if $game_temp.player_transferring or
       $game_temp.message_window_showing or
       $game_temp.transition_processing
      # End
      return false
    end
    # Set transferring player flag
    $game_temp.player_transferring = true
    # If appointment method is [direct appointment]
    if @parameters[0] == 0
      # Set player move destination
      $game_temp.player_new_map_id = @parameters[1]
      $game_temp.player_new_x = @parameters[2]
      $game_temp.player_new_y = @parameters[3]
      $game_temp.player_new_direction = @parameters[4]
    # If appointment method is [appoint with variables]
    else
      # Set player move destination
      $game_temp.player_new_map_id = $game_variables[@parameters[1]]
      $game_temp.player_new_x = $game_variables[@parameters[2]]
      $game_temp.player_new_y = $game_variables[@parameters[3]]
      $game_temp.player_new_direction = @parameters[4]
    end
    # Advance index
    @index += 1
    # If fade is set
    if @parameters[5] == 0
      # Prepare for transition
      Graphics.freeze
      # Set transition processing flag
      $game_temp.transition_processing = true
      $game_temp.transition_name = ""
    end
    # End
    return false
  end
  #--------------------------------------------------------------------------
  # * Set Event Location
  #--------------------------------------------------------------------------
  def command_202
    # If in battle
    if $game_temp.in_battle
      # Continue
      return true
    end
    # Get character
    character = get_character(@parameters[0])
    # If no character exists
    if character == nil
      # Continue
      return true
    end
    # If appointment method is [direct appointment]
    if @parameters[1] == 0
      # Set character position
      character.moveto(@parameters[2], @parameters[3])
    # If appointment method is [appoint with variables]
    elsif @parameters[1] == 1
      # Set character position
      character.moveto($game_variables[@parameters[2]],
        $game_variables[@parameters[3]])
    # If appointment method is [exchange with another event]
    else
      old_x = character.x
      old_y = character.y
      character2 = get_character(@parameters[2])
      if character2 != nil
        character.moveto(character2.x, character2.y)
        character2.moveto(old_x, old_y)
      end
    end
    # Set character direction
    case @parameters[4]
    when 8  # up
      character.turn_up
    when 6  # right
      character.turn_right
    when 2  # down
      character.turn_down
    when 4  # left
      character.turn_left
    end
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Scroll Map
  #--------------------------------------------------------------------------
  def command_203
    # If in battle
    if $game_temp.in_battle
      # Continue
      return true
    end
    # If already scrolling
    if $game_map.scrolling?
      # End
      return false
    end
    # Start scroll
    $game_map.start_scroll(@parameters[0], @parameters[1], @parameters[2])
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Change Map Settings
  #--------------------------------------------------------------------------
  def command_204
    case @parameters[0]
    when 0  # panorama
      $game_map.panorama_name = @parameters[1]
      $game_map.panorama_hue = @parameters[2]
    when 1  # fog
      $game_map.fog_name = @parameters[1]
      $game_map.fog_hue = @parameters[2]
      $game_map.fog_opacity = @parameters[3]
      $game_map.fog_blend_type = @parameters[4]
      $game_map.fog_zoom = @parameters[5]
      $game_map.fog_sx = @parameters[6]
      $game_map.fog_sy = @parameters[7]
    when 2  # battleback
      $game_map.battleback_name = @parameters[1]
      $game_temp.battleback_name = @parameters[1]
    end
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Change Fog Color Tone
  #--------------------------------------------------------------------------
  def command_205
    # Start color tone change
    $game_map.start_fog_tone_change(@parameters[0], @parameters[1] * 2)
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Change Fog Opacity
  #--------------------------------------------------------------------------
  def command_206
    # Start opacity level change
    $game_map.start_fog_opacity_change(@parameters[0], @parameters[1] * 2)
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Show Animation
  #--------------------------------------------------------------------------
  def command_207
    # Get character
    character = get_character(@parameters[0])
    # If no character exists
    if character == nil
      # Continue
      return true
    end
    # Set animation ID
    character.animation_id = @parameters[1]
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Change Transparent Flag
  #--------------------------------------------------------------------------
  def command_208
    # Change player transparent flag
    $game_player.transparent = (@parameters[0] == 0)
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Set Move Route
  #--------------------------------------------------------------------------
  def command_209
    # Get character
    character = get_character(@parameters[0])
    # If no character exists
    if character == nil
      # Continue
      return true
    end
    # Force move route
    character.force_move_route(@parameters[1])
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Wait for Move's Completion
  #--------------------------------------------------------------------------
  def command_210
    # If not in battle
    unless $game_temp.in_battle
      # Set move route completion waiting flag
      @move_route_waiting = true
    end
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Prepare for Transition
  #--------------------------------------------------------------------------
  def command_221
    # If showing message window
    if $game_temp.message_window_showing
      # End
      return false
    end
    # Prepare for transition
    Graphics.freeze
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Execute Transition
  #--------------------------------------------------------------------------
  def command_222
    # If transition processing flag is already set
    if $game_temp.transition_processing
      # End
      return false
    end
    # Set transition processing flag
    $game_temp.transition_processing = true
    $game_temp.transition_name = @parameters[0]
    # Advance index
    @index += 1
    # End
    return false
  end
  #--------------------------------------------------------------------------
  # * Change Screen Color Tone
  #--------------------------------------------------------------------------
  def command_223
    # Start changing color tone
    $game_screen.start_tone_change(@parameters[0], @parameters[1] * 2)
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Screen Flash
  #--------------------------------------------------------------------------
  def command_224
    # Start flash
    $game_screen.start_flash(@parameters[0], @parameters[1] * 2)
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Screen Shake
  #--------------------------------------------------------------------------
  def command_225
    # Start shake
    $game_screen.start_shake(@parameters[0], @parameters[1],
      @parameters[2] * 2)
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Show Picture
  #--------------------------------------------------------------------------
  def command_231
    # Get picture number
    number = @parameters[0] + ($game_temp.in_battle ? 50 : 0)
    # If appointment method is [direct appointment]
    if @parameters[3] == 0
      x = @parameters[4]
      y = @parameters[5]
    # If appointment method is [appoint with variables]
    else
      x = $game_variables[@parameters[4]]
      y = $game_variables[@parameters[5]]
    end
    # Show picture
    $game_screen.pictures[number].show(@parameters[1], @parameters[2],
      x, y, @parameters[6], @parameters[7], @parameters[8], @parameters[9])
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Move Picture
  #--------------------------------------------------------------------------
  def command_232
    # Get picture number
    number = @parameters[0] + ($game_temp.in_battle ? 50 : 0)
    # If appointment method is [direct appointment]
    if @parameters[3] == 0
      x = @parameters[4]
      y = @parameters[5]
    # If appointment method is [appoint with variables]
    else
      x = $game_variables[@parameters[4]]
      y = $game_variables[@parameters[5]]
    end
    # Move picture
    $game_screen.pictures[number].move(@parameters[1] * 2, @parameters[2],
      x, y, @parameters[6], @parameters[7], @parameters[8], @parameters[9])
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Rotate Picture
  #--------------------------------------------------------------------------
  def command_233
    # Get picture number
    number = @parameters[0] + ($game_temp.in_battle ? 50 : 0)
    # Set rotation speed
    $game_screen.pictures[number].rotate(@parameters[1])
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Change Picture Color Tone
  #--------------------------------------------------------------------------
  def command_234
    # Get picture number
    number = @parameters[0] + ($game_temp.in_battle ? 50 : 0)
    # Start changing color tone
    $game_screen.pictures[number].start_tone_change(@parameters[1],
      @parameters[2] * 2)
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Erase Picture
  #--------------------------------------------------------------------------
  def command_235
    # Get picture number
    number = @parameters[0] + ($game_temp.in_battle ? 50 : 0)
    # Erase picture
    $game_screen.pictures[number].erase
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Set Weather Effects
  #--------------------------------------------------------------------------
  def command_236
    # Set Weather Effects
    $game_screen.weather(@parameters[0], @parameters[1], @parameters[2])
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Play BGM
  #--------------------------------------------------------------------------
  def command_241
    # Play BGM
    $game_system.bgm_play(@parameters[0])
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Fade Out BGM
  #--------------------------------------------------------------------------
  def command_242
    # Fade out BGM
    $game_system.bgm_fade(@parameters[0])
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Play BGS
  #--------------------------------------------------------------------------
  def command_245
    # Play BGS
    $game_system.bgs_play(@parameters[0])
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Fade Out BGS
  #--------------------------------------------------------------------------
  def command_246
    # Fade out BGS
    $game_system.bgs_fade(@parameters[0])
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Memorize BGM/BGS
  #--------------------------------------------------------------------------
  def command_247
    # Memorize BGM/BGS
    $game_system.bgm_memorize
    $game_system.bgs_memorize
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Restore BGM/BGS
  #--------------------------------------------------------------------------
  def command_248
    # Restore BGM/BGS
    $game_system.bgm_restore
    $game_system.bgs_restore
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Play ME
  #--------------------------------------------------------------------------
  def command_249
    # Play ME
    $game_system.me_play(@parameters[0])
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Play SE
  #--------------------------------------------------------------------------
  def command_250
    # Play SE
    $game_system.se_play(@parameters[0])
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Stop SE
  #--------------------------------------------------------------------------
  def command_251
    # Stop SE
    Audio.se_stop
    # Continue
    return true
  end
end

#==============================================================================
# ** Interpreter (part 6)
#------------------------------------------------------------------------------
#  This interpreter runs event commands. This class is used within the
#  Game_System class and the Game_Event class.
#==============================================================================

class Interpreter
  #--------------------------------------------------------------------------
  # * Battle Processing
  #--------------------------------------------------------------------------
  def command_301
    # If not invalid troops
    if $data_troops[@parameters[0]] != nil
      # Set battle abort flag
      $game_temp.battle_abort = true
      # Set battle calling flag
      $game_temp.battle_calling = true
      $game_temp.battle_troop_id = @parameters[0]
      $game_temp.battle_can_escape = @parameters[1]
      $game_temp.battle_can_lose = @parameters[2]
      # Set callback
      current_indent = @list[@index].indent
      $game_temp.battle_proc = Proc.new { |n| @branch[current_indent] = n }
    end
    # Advance index
    @index += 1
    # End
    return false
  end
  #--------------------------------------------------------------------------
  # * If Win
  #--------------------------------------------------------------------------
  def command_601
    # When battle results = win
    if @branch[@list[@index].indent] == 0
      # Delete branch data
      @branch.delete(@list[@index].indent)
      # Continue
      return true
    end
    # If it doesn't meet conditions: command skip
    return command_skip
  end
  #--------------------------------------------------------------------------
  # * If Escape
  #--------------------------------------------------------------------------
  def command_602
    # If battle results = escape
    if @branch[@list[@index].indent] == 1
      # Delete branch data
      @branch.delete(@list[@index].indent)
      # Continue
      return true
    end
    # If it doesn't meet conditions: command skip
    return command_skip
  end
  #--------------------------------------------------------------------------
  # * If Lose
  #--------------------------------------------------------------------------
  def command_603
    # If battle results = lose
    if @branch[@list[@index].indent] == 2
      # Delete branch data
      @branch.delete(@list[@index].indent)
      # Continue
      return true
    end
    # If it doesn't meet conditions: command skip
    return command_skip
  end
  #--------------------------------------------------------------------------
  # * Shop Processing
  #--------------------------------------------------------------------------
  def command_302
    # Set battle abort flag
    $game_temp.battle_abort = true
    # Set shop calling flag
    $game_temp.shop_calling = true
    # Set goods list on new item
    $game_temp.shop_goods = [@parameters]
    # Loop
    loop do
      # Advance index
      @index += 1
      # If next event command has shop on second line or after
      if @list[@index].code == 605
        # Add goods list to new item
        $game_temp.shop_goods.push(@list[@index].parameters)
      # If event command does not have shop on second line or after
      else
        # End
        return false
      end
    end
  end
  #--------------------------------------------------------------------------
  # * Name Input Processing
  #--------------------------------------------------------------------------
  def command_303
    # If not invalid actors
    if $data_actors[@parameters[0]] != nil
      # Set battle abort flag
      $game_temp.battle_abort = true
      # Set name input calling flag
      $game_temp.name_calling = true
      $game_temp.name_actor_id = @parameters[0]
      $game_temp.name_max_char = @parameters[1]
    end
    # Advance index
    @index += 1
    # End
    return false
  end
  #--------------------------------------------------------------------------
  # * Change HP
  #--------------------------------------------------------------------------
  def command_311
    # Get operate value
    value = operate_value(@parameters[1], @parameters[2], @parameters[3])
    # Process with iterator
    iterate_actor(@parameters[0]) do |actor|
      # If HP are not 0
      if actor.hp > 0
        # Change HP (if death is not permitted, make HP 1)
        if @parameters[4] == false and actor.hp + value <= 0
          actor.hp = 1
        else
          actor.hp += value
        end
      end
    end
    # Determine game over
    $game_temp.gameover = $game_party.all_dead?
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Change SP
  #--------------------------------------------------------------------------
  def command_312
    # Get operate value
    value = operate_value(@parameters[1], @parameters[2], @parameters[3])
    # Process with iterator
    iterate_actor(@parameters[0]) do |actor|
      # Change actor SP
      actor.sp += value
    end
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Change State
  #--------------------------------------------------------------------------
  def command_313
    # Process with iterator
    iterate_actor(@parameters[0]) do |actor|
      # Change state
      if @parameters[1] == 0
        actor.add_state(@parameters[2])
      else
        actor.remove_state(@parameters[2])
      end
    end
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Recover All
  #--------------------------------------------------------------------------
  def command_314
    # Process with iterator
    iterate_actor(@parameters[0]) do |actor|
      # Recover all for actor
      actor.recover_all
    end
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Change EXP
  #--------------------------------------------------------------------------
  def command_315
    # Get operate value
    value = operate_value(@parameters[1], @parameters[2], @parameters[3])
    # Process with iterator
    iterate_actor(@parameters[0]) do |actor|
      # Change actor EXP
      actor.exp += value
    end
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Change Level
  #--------------------------------------------------------------------------
  def command_316
    # Get operate value
    value = operate_value(@parameters[1], @parameters[2], @parameters[3])
    # Process with iterator
    iterate_actor(@parameters[0]) do |actor|
      # Change actor level
      actor.level += value
    end
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Change Parameters
  #--------------------------------------------------------------------------
  def command_317
    # Get operate value
    value = operate_value(@parameters[2], @parameters[3], @parameters[4])
    # Get actor
    actor = $game_actors[@parameters[0]]
    # Change parameters
    if actor != nil
      case @parameters[1]
      when 0  # MaxHP
        actor.maxhp += value
      when 1  # MaxSP
        actor.maxsp += value
      when 2  # strength
        actor.str += value
      when 3  # dexterity
        actor.dex += value
      when 4  # agility
        actor.agi += value
      when 5  # intelligence
        actor.int += value
      end
    end
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Change Skills
  #--------------------------------------------------------------------------
  def command_318
    # Get actor
    actor = $game_actors[@parameters[0]]
    # Change skill
    if actor != nil
      if @parameters[1] == 0
        actor.learn_skill(@parameters[2])
      else
        actor.forget_skill(@parameters[2])
      end
    end
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Change Equipment
  #--------------------------------------------------------------------------
  def command_319
    # Get actor
    actor = $game_actors[@parameters[0]]
    # Change Equipment
    if actor != nil
      actor.equip(@parameters[1], @parameters[2])
    end
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Change Actor Name
  #--------------------------------------------------------------------------
  def command_320
    # Get actor
    actor = $game_actors[@parameters[0]]
    # Change name
    if actor != nil
      actor.name = @parameters[1]
    end
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Change Actor Class
  #--------------------------------------------------------------------------
  def command_321
    # Get actor
    actor = $game_actors[@parameters[0]]
    # Change class
    if actor != nil
      actor.class_id = @parameters[1]
    end
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Change Actor Graphic
  #--------------------------------------------------------------------------
  def command_322
    # Get actor
    actor = $game_actors[@parameters[0]]
    # Change graphic
    if actor != nil
      actor.set_graphic(@parameters[1], @parameters[2],
        @parameters[3], @parameters[4])
    end
    # Refresh player
    $game_player.refresh
    # Continue
    return true
  end
end

#==============================================================================
# ** Interpreter (part 7)
#------------------------------------------------------------------------------
#  This interpreter runs event commands. This class is used within the
#  Game_System class and the Game_Event class.
#==============================================================================

class Interpreter
  #--------------------------------------------------------------------------
  # * Change Enemy HP
  #--------------------------------------------------------------------------
  def command_331
    # Get operate value
    value = operate_value(@parameters[1], @parameters[2], @parameters[3])
    # Process with iterator
    iterate_enemy(@parameters[0]) do |enemy|
      # If HP is not 0
      if enemy.hp > 0
        # Change HP (if death is not permitted then change HP to 1)
        if @parameters[4] == false and enemy.hp + value <= 0
          enemy.hp = 1
        else
          enemy.hp += value
        end
      end
    end
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Change Enemy SP
  #--------------------------------------------------------------------------
  def command_332
    # Get operate value
    value = operate_value(@parameters[1], @parameters[2], @parameters[3])
    # Process with iterator
    iterate_enemy(@parameters[0]) do |enemy|
      # Change SP
      enemy.sp += value
    end
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Change Enemy State
  #--------------------------------------------------------------------------
  def command_333
    # Process with iterator
    iterate_enemy(@parameters[0]) do |enemy|
      # If [regard HP 0] state option is valid
      if $data_states[@parameters[2]].zero_hp
        # Clear immortal flag
        enemy.immortal = false
      end
      # Change
      if @parameters[1] == 0
        enemy.add_state(@parameters[2])
      else
        enemy.remove_state(@parameters[2])
      end
    end
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Enemy Recover All
  #--------------------------------------------------------------------------
  def command_334
    # Process with iterator
    iterate_enemy(@parameters[0]) do |enemy|
      # Recover all
      enemy.recover_all
    end
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Enemy Appearance
  #--------------------------------------------------------------------------
  def command_335
    # Get enemy
    enemy = $game_troop.enemies[@parameters[0]]
    # Clear hidden flag
    if enemy != nil
      enemy.hidden = false
    end
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Enemy Transform
  #--------------------------------------------------------------------------
  def command_336
    # Get enemy
    enemy = $game_troop.enemies[@parameters[0]]
    # Transform processing
    if enemy != nil
      enemy.transform(@parameters[1])
    end
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Show Battle Animation
  #--------------------------------------------------------------------------
  def command_337
    # Process with iterator
    iterate_battler(@parameters[0], @parameters[1]) do |battler|
      # If battler exists
      if battler.exist?
        # Set animation ID
        battler.animation_id = @parameters[2]
      end
    end
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Deal Damage
  #--------------------------------------------------------------------------
  def command_338
    # Get operate value
    value = operate_value(0, @parameters[2], @parameters[3])
    # Process with iterator
    iterate_battler(@parameters[0], @parameters[1]) do |battler|
      # If battler exists
      if battler.exist?
        # Change HP
        battler.hp -= value
        # If in battle
        if $game_temp.in_battle
          # Set damage
          battler.damage = value
          battler.damage_pop = true
        end
      end
    end
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Force Action
  #--------------------------------------------------------------------------
  def command_339
    # Ignore if not in battle
    unless $game_temp.in_battle
      return true
    end
    # Ignore if number of turns = 0
    if $game_temp.battle_turn == 0
      return true
    end
    # Process with iterator (For convenience, this process won't be repeated)
    iterate_battler(@parameters[0], @parameters[1]) do |battler|
      # If battler exists
      if battler.exist?
        # Set action
        battler.current_action.kind = @parameters[2]
        if battler.current_action.kind == 0
          battler.current_action.basic = @parameters[3]
        else
          battler.current_action.skill_id = @parameters[3]
        end
        # Set action target
        if @parameters[4] == -2
          if battler.is_a?(Game_Enemy)
            battler.current_action.decide_last_target_for_enemy
          else
            battler.current_action.decide_last_target_for_actor
          end
        elsif @parameters[4] == -1
          if battler.is_a?(Game_Enemy)
            battler.current_action.decide_random_target_for_enemy
          else
            battler.current_action.decide_random_target_for_actor
          end
        elsif @parameters[4] >= 0
          battler.current_action.target_index = @parameters[4]
        end
        # Set force flag
        battler.current_action.forcing = true
        # If action is valid and [run now]
        if battler.current_action.valid? and @parameters[5] == 1
          # Set battler being forced into action
          $game_temp.forcing_battler = battler
          # Advance index
          @index += 1
          # End
          return false
        end
      end
    end
    # Continue
    return true
  end
  #--------------------------------------------------------------------------
  # * Abort Battle
  #--------------------------------------------------------------------------
  def command_340
    # Set battle abort flag
    $game_temp.battle_abort = true
    # Advance index
    @index += 1
    # End
    return false
  end
end
