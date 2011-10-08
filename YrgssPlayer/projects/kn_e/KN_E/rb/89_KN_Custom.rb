#==============================================================================
# ■ KN_Custom
#------------------------------------------------------------------------------
# 　サンプルゲーム『KNight-Blade Howling of Kerberos』専用のスクリプトです。
#==============================================================================

class Game_Player < Game_Character
  #--------------------------------------------------------------------------
  # ● 再定義 : 通行可能判定
  #--------------------------------------------------------------------------
  def passable?(x, y, d)
    new_x = x + (d == 6 ? 1 : d == 4 ? -1 : 0)
    new_y = y + (d == 2 ? 1 : d == 8 ? -1 : 0)
    unless $game_map.valid?(new_x, new_y)
      return false
    end
    if $DEBUG and Input.press?(Input::CTRL)
      return true
    end
    # パーティ内にアクター 4 番がいるとき、地形タグ 1 番を通行不可にする
    if $game_party.actors.include?($game_actors[4]) and
       $game_map.terrain_tag(new_x, new_y) == 1
      return false
    end
    # パーティ内にアクター 5 番がいるとき、地形タグ 1 番を通行不可にする
    if $game_party.actors.include?($game_actors[5]) and
       $game_map.terrain_tag(new_x, new_y) == 1
      return false
    end
    # パーティ内にアクター 6 番がいるとき、地形タグ 1 番を通行不可にする
    if $game_party.actors.include?($game_actors[6]) and
       $game_map.terrain_tag(new_x, new_y) == 1
      return false
    end
    # パーティ内にアクター 7 番がいるとき、地形タグ 1 番を通行不可にする
    if $game_party.actors.include?($game_actors[7]) and
       $game_map.terrain_tag(new_x, new_y) == 1
      return false
    end
    # パーティ内にアクター 8 番がいるとき、地形タグ 1 番を通行不可にする
    if $game_party.actors.include?($game_actors[8]) and
       $game_map.terrain_tag(new_x, new_y) == 1
      return false
    end
    super
  end

  #--------------------------------------------------------------------------
  # ● 再定義 : フレーム更新
  #--------------------------------------------------------------------------
  alias orig_update update
  def update
    # パーティ内にアクター 4 番がいるとき、A ボタンでダッシュ
    if $game_party.actors.include?($game_actors[4])
      unless $game_system.map_interpreter.running? or
             @move_route_forcing or $game_temp.message_window_showing
        if Input.dir4 == 0
          if Input.trigger?(Input::A)
            @move_speed = 5
            @character_name = "ag0_b"
            @dash = true
          elsif not Input.press?(Input::A)
            @move_speed = 3
            @character_name = "ag0"
            @dash = false
          end
        end
      else
        # イベント中はダッシュ無効
        if @dash
          @move_speed = 3
          @character_name = "ag0"
          @dash = false
        end
      end
      # ダッシュ中は音を鳴らす
      if @dash
        Audio.se_play("Audio/SE/dash", 80, 150)
      end
    end

    # パーティ内にアクター 5 番がいるとき、A ボタンでダッシュ
    if $game_party.actors.include?($game_actors[5])
      unless $game_system.map_interpreter.running? or
             @move_route_forcing or $game_temp.message_window_showing
        if Input.dir4 == 0
          if Input.trigger?(Input::A)
            @move_speed = 5
            @character_name = "ag_c_b"
            @dash = true
          elsif not Input.press?(Input::A)
            @move_speed = 3
            @character_name = "ag_c"
            @dash = false
          end
        end
      else
        # イベント中はダッシュ無効
        if @dash
          @move_speed = 3
          @character_name = "ag_c"
          @dash = false
        end
      end
      # ダッシュ中は音を鳴らす
      if @dash
        Audio.se_play("Audio/SE/dash", 80, 150)
      end
    end
    # パーティ内にアクター 6 番がいるとき、A ボタンでダッシュ
    if $game_party.actors.include?($game_actors[6])
      unless $game_system.map_interpreter.running? or
             @move_route_forcing or $game_temp.message_window_showing
        if Input.dir4 == 0
          if Input.trigger?(Input::A)
            @move_speed = 5
            @character_name = "ag2_b"
            @dash = true
          elsif not Input.press?(Input::A)
            @move_speed = 3
            @character_name = "ag2"
            @dash = false
          end
        end
      else
        # イベント中はダッシュ無効
        if @dash
          @move_speed = 3
          @character_name = "ag2"
          @dash = false
        end
      end
      # ダッシュ中は音を鳴らす
      if @dash
        Audio.se_play("Audio/SE/dash", 80, 150)
      end
    end
    # パーティ内にアクター 7 番がいるとき、A ボタンでダッシュ
    if $game_party.actors.include?($game_actors[7])
      unless $game_system.map_interpreter.running? or
             @move_route_forcing or $game_temp.message_window_showing
        if Input.dir4 == 0
          if Input.trigger?(Input::A)
            @move_speed = 5
            @character_name = "ag3_b"
            @dash = true
          elsif not Input.press?(Input::A)
            @move_speed = 3
            @character_name = "ag3"
            @dash = false
          end
        end
      else
        # イベント中はダッシュ無効
        if @dash
          @move_speed = 3
          @character_name = "ag3"
          @dash = false
        end
      end
      # ダッシュ中は音を鳴らす
      if @dash
        Audio.se_play("Audio/SE/dash", 80, 150)
      end
    end
    # パーティ内にアクター 8 番がいるとき、A ボタンでダッシュ
    if $game_party.actors.include?($game_actors[8])
      unless $game_system.map_interpreter.running? or
             @move_route_forcing or $game_temp.message_window_showing
        if Input.dir4 == 0
          if Input.trigger?(Input::A)
            @move_speed = 6
            @character_name = "ag4_b"
            @dash = true
          elsif not Input.press?(Input::A)
            @move_speed = 4
            @character_name = "ag4"
            @dash = false
          end
        end
      else
        # イベント中はダッシュ無効
        if @dash
          @move_speed = 4
          @character_name = "ag4"
          @dash = false
        end
      end
      # ダッシュ中は音を鳴らす
      if @dash
        Audio.se_play("Audio/SE/dash", 80, 150)
      end
    end

    # 元の定義を呼び出す
    orig_update
  end
end

class Window_Base < Window
  #--------------------------------------------------------------------------
  # ● 再定義 グラフィックの描画
  #     actor : アクター
  #     x     : 描画先 X 座標
  #     y     : 描画先 Y 座標
  #--------------------------------------------------------------------------
 def draw_actor_graphic(actor, x, y)
    bitmap = RPG::Cache.battler(actor.battler_name, actor.battler_hue) #(actor.character_name, actor.character_hue)
    cw = bitmap.width
    ch = bitmap.height
    src_rect = Rect.new(0, 0, cw, ch)
    self.contents.blt(x - cw /4, y - ch +16, bitmap, src_rect)
  end
end

class Window_Status < Window_Base
  #--------------------------------------------------------------------------
  # ● リフレッシュ
  #--------------------------------------------------------------------------
  def refresh
    self.contents.clear
    draw_actor_graphic(@actor, 30, 144)
    draw_actor_name(@actor, 4, 8)
    draw_actor_class(@actor, 4 + 124, 8)
    #draw_actor_level(@actor, 96, 32)
    draw_actor_state(@actor, 128, 56)
    draw_actor_hp(@actor, 128, 96, 172)
    draw_actor_sp(@actor, 128, 128, 172)
    draw_actor_parameter(@actor, 352, 240, 0)
    draw_actor_parameter(@actor, 352, 272, 1)
    draw_actor_parameter(@actor, 352, 304, 2)
    draw_actor_parameter(@actor, 352, 80, 3)
    draw_actor_parameter(@actor, 352, 112, 4)
    draw_actor_parameter(@actor, 352, 144, 5)
    #draw_actor_parameter(@actor, 128, 384, 6)
    self.contents.font.color = system_color
    #self.contents.draw_text(320, 48, 80, 32, "--")#EXP
    #self.contents.draw_text(320, 80, 80, 32, "--")#NEXT
    self.contents.font.color = normal_color
    #self.contents.draw_text(320 + 80, 48, 84, 32, @actor.exp_s, 2)
    #self.contents.draw_text(320 + 80, 80, 84, 32, @actor.next_rest_exp_s, 2)
    self.contents.draw_text(128, 208, 96, 32, "[Equip]")
    draw_item_name($data_weapons[@actor.weapon_id], 112 + 16, 240)
    draw_item_name($data_armors[@actor.armor1_id], 112 + 16, 272)
    draw_item_name($data_armors[@actor.armor2_id], 112 + 16, 304)
    draw_item_name($data_armors[@actor.armor3_id], 112 + 16, 352)
    draw_item_name($data_armors[@actor.armor4_id], 112 + 16, 384)
  end
  def dummy
    self.contents.font.color = system_color
    self.contents.draw_text(352, 80, 96, 32, $data_system.words.weapon)
    self.contents.draw_text(352, 112, 96, 32, $data_system.words.armor1)
    self.contents.draw_text(352, 144, 96, 32, $data_system.words.armor2)
    self.contents.draw_text(352, 176, 96, 32, $data_system.words.armor3)
    self.contents.draw_text(352, 208, 96, 32, $data_system.words.armor4)
    draw_item_name($data_weapons[@actor.weapon_id], 352 + 24, 80)
    draw_item_name($data_armors[@actor.armor1_id], 352 + 24, 112)
    draw_item_name($data_armors[@actor.armor2_id], 352 + 24, 144)
    draw_item_name($data_armors[@actor.armor3_id], 352 + 24, 176)
    draw_item_name($data_armors[@actor.armor4_id], 352 + 24, 208)
  end
end

class Window_Base < Window
  #--------------------------------------------------------------------------
  # ● 再定義 クラスの描画
  #     actor : アクター
  #     x     : 描画先 X 座標
  #     y     : 描画先 Y 座標
  #--------------------------------------------------------------------------
  def draw_actor_class(actor, x, y)
    self.contents.font.color = normal_color
    self.contents.draw_text(x, y, 236, 32, actor.class_name)
  end
end
  
class Window_MenuStatus < Window_Selectable
  #--------------------------------------------------------------------------
  # ● 再定義 リフレッシュ
  #--------------------------------------------------------------------------
  def refresh
    self.contents.clear
    @item_max = $game_party.actors.size
    for i in 0...$game_party.actors.size
      x = 64
      y = i * 116
      actor = $game_party.actors[i]
      draw_actor_graphic(actor, x - 40, y + 80)
      draw_actor_name(actor, x + 64, y) #16 draw_actor_name(actor, x, y)
      draw_actor_class(actor, x + 64, y + 32) #90
      #draw_actor_level(actor, x + 16, y + 32) #draw_actor_level(actor, x, y + 32)
      draw_actor_state(actor, x + 210, y) #144
      #draw_actor_exp(actor, x + 16, y + 64) #draw_actor_exp(actor, x, y + 64)
      draw_actor_hp(actor, x + 210, y + 32) #236
      draw_actor_sp(actor, x + 210, y + 64) #236
   end
  end
end

class Window_Target < Window_Selectable
  #--------------------------------------------------------------------------
  # ● 再定義 リフレッシュ
  #--------------------------------------------------------------------------
  def refresh
    self.contents.clear
    for i in 0...$game_party.actors.size
      x = 4
      y = i * 116
      actor = $game_party.actors[i]
      draw_actor_name(actor, x, y)
      draw_actor_class(actor, x + 144, y)
      #draw_actor_level(actor, x + 8, y + 32)
      draw_actor_state(actor, x + 8, y + 64)
      draw_actor_hp(actor, x + 152, y + 32)
      draw_actor_sp(actor, x + 152, y + 64)
    end
  end  
end

class Window_PartyCommand < Window_Selectable
  #--------------------------------------------------------------------------
  # ● 再定義 オブジェクト初期化
  #--------------------------------------------------------------------------
  def initialize
    super(0, 0, 640, 64)
    self.contents = Bitmap.new(width - 32, height - 32)
    self.back_opacity = 160
    @commands = ["Fight", "Escape"]
    @item_max = 2
    @column_max = 2
    draw_item(0, normal_color)
    draw_item(1, $game_temp.battle_can_escape ? normal_color : disabled_color)
    self.active = false
    self.visible = false
    self.index = 0
  end
end

class Window_EquipLeft < Window_Base
  #--------------------------------------------------------------------------
  # ● 再定義 リフレッシュ
  #--------------------------------------------------------------------------
  def refresh
    self.contents.clear
    draw_actor_name(@actor, 4, 0)
    draw_actor_class(@actor, 4, 32)
    draw_actor_parameter(@actor, 4, 64, 0)
    draw_actor_parameter(@actor, 4, 96, 1)
    draw_actor_parameter(@actor, 4, 128, 2)
    if @new_atk != nil
      self.contents.font.color = system_color
      self.contents.draw_text(160, 64, 40, 32, ">>", 1)
      self.contents.font.color = normal_color
      self.contents.draw_text(200, 64, 36, 32, @new_atk.to_s, 2)
    end
    if @new_pdef != nil
      self.contents.font.color = system_color
      self.contents.draw_text(160, 96, 40, 32, ">>", 1)
      self.contents.font.color = normal_color
      self.contents.draw_text(200, 96, 36, 32, @new_pdef.to_s, 2)
    end
    if @new_mdef != nil
      self.contents.font.color = system_color
      self.contents.draw_text(160, 128, 40, 32, ">>", 1)
      self.contents.font.color = normal_color
      self.contents.draw_text(200, 128, 36, 32, @new_mdef.to_s, 2)
    end
  end
end

class Window_Message < Window_Selectable
  #--------------------------------------------------------------------------
  # ● 再定義 リフレッシュ
  #--------------------------------------------------------------------------
  def refresh
    self.contents.clear
    self.contents.font.color = normal_color
    x = y = 0
    @cursor_width = 0
    # 選択肢なら字下げを行う
    if $game_temp.choice_start == 0
      x = 8
    end
    # 表示待ちのメッセージがある場合
    if $game_temp.message_text != nil
      text = $game_temp.message_text
      # 制御文字処理
      begin
        last_text = text.clone
        text.gsub!(/\\[Vv]\[([0-9]+)\]/) { $game_variables[$1.to_i] }
      end until text == last_text
      text.gsub!(/\\[Nn]\[([0-9]+)\]/) do
        $game_actors[$1.to_i] != nil ? $game_actors[$1.to_i].name : ""
      end
      # 便宜上、"\\C" を "\001" に、"\\G" を "\002" に変換
      text.gsub!(/\\[Cc]\[([0-9]+)\]/) { "\001[#{$1}]" }
      text.gsub!(/\\[Gg]/) { "\002" }
      text.gsub!(/\\\\/) { "\\" }
      # c に 1 文字を取得 (文字が取得できなくなるまでループ)
      while ((c = text.slice!(/./m)) != nil)
        # \C[n] の場合
        if c == "\001"
          # 文字色を変更
          text.sub!(/\[([0-9]+)\]/, "")
          color = $1.to_i
          if color >= 0 and color <= 7
            self.contents.font.color = text_color(color)
          end
          # 次の文字へ
          next
        end
        # \G の場合
        if c == "\002"
          # ゴールドウィンドウを作成
          if @gold_window == nil
            @gold_window = Window_Gold.new
            @gold_window.x = 560 - @gold_window.width
            @gold_window.y = self.y >= 128 ? 32 : 288#384
            @gold_window.opacity = self.opacity
          end
          # 次の文字へ
          next
        end
        # 改行文字の場合
        if c == "\n"
          # 選択肢ならカーソルの幅を更新
          if y >= $game_temp.choice_start
            @cursor_width = [@cursor_width, x].max
          end
          # y に 1 を加算
          y += 1
          x = 0
          # 選択肢なら字下げを行う
          if y >= $game_temp.choice_start
            x = 8
          end
          # 次の文字へ
          next
        end
        # 文字を描画
        self.contents.draw_text(4 + x, 32 * y, 40, 32, c)
        # 全角スペースの場合
        if c == "　"
          # x に "「" 文字の幅を加算
          x += self.contents.text_size("「").width
        else
          # x に描画した文字の幅を加算
          x += self.contents.text_size(c).width
        end
      end
    end
    # 選択肢の場合
    if $game_temp.choice_max > 0
      @item_max = $game_temp.choice_max
      self.active = true
      self.index = 0
    end
    # 数値入力の場合
    if $game_temp.num_input_variable_id > 0
      digits_max = $game_temp.num_input_digits_max
      number = $game_variables[$game_temp.num_input_variable_id]
      @input_number_window = Window_InputNumber.new(digits_max)
      @input_number_window.number = number
      @input_number_window.x = self.x + 8
      @input_number_window.y = self.y + $game_temp.num_input_start * 32
    end
  end
end

class Window_BattleStatus < Window_Base
  #--------------------------------------------------------------------------
  # ● 再定義 リフレッシュ（戦闘時のHP,SP表示位置）
  #--------------------------------------------------------------------------
  def refresh
    self.contents.clear
    @item_max = $game_party.actors.size
    for i in 0...$game_party.actors.size
      actor = $game_party.actors[i]
      actor_x = i * 160 #+ 4
    #self.contents.font.color = system_color
    #draw_actor_name(actor, actor_x, 0)
    self.contents.font.size = 18
    draw_actor_hp(actor, actor_x , 0)
    draw_actor_sp(actor, actor_x , 24)
    #self.contents.font.size = 20
    #draw_actor_hp(actor, actor_x , 30)
    #draw_actor_sp(actor, actor_x , 56)

    #self.contents.font.color = system_color
    #self.contents.draw_text(actor_x , 32, 32, 32, $data_system.words.hp)
    #self.contents.draw_text(actor_x , 60, 32, 32, $data_system.words.sp)
        #self.contents.font.color = normal_color
        #self.contents.draw_text(actor_x + 80, -12, 64, 120, "/")
        #self.contents.draw_text(actor_x + 80, 15, 64, 120, "/")
        #self.contents.draw_text(actor_x + 96, -12, 64, 120, actor.maxhp.to_s)
        #self.contents.draw_text(actor_x + 96, 15, 64, 120, actor.maxsp.to_s)
        #self.contents.font.color = actor.hp == 0 ? knockout_color :
        #actor.hp <= actor.maxhp / 4 ? crisis_color : normal_color
        #self.contents.draw_text(actor_x + 34, 32, 48, 32, actor.hp.to_s)
        #self.contents.font.color = actor.sp == 0 ? knockout_color :
        #actor.sp <= actor.maxsp / 4 ? crisis_color : normal_color
        #self.contents.draw_text(actor_x + 34, 60, 48, 32, actor.sp.to_s)
      if @level_up_flags[i]
        self.contents.font.color = normal_color
        self.contents.draw_text(actor_x, 96, 120, 32, "LEVEL UP!")
      else
        self.contents.font.size = 15
        draw_actor_state(actor, actor_x, 100)
      end
    end
  end
end

class Scene_Title
  #--------------------------------------------------------------------------
  # ● 再定義 メイン処理
  #--------------------------------------------------------------------------
  def main
    # 戦闘テストの場合
    if $BTEST
      battle_test
      return
    end
    # データベースをロード
    $data_actors        = load_data("Data/Actors.rxdata")
    $data_classes       = load_data("Data/Classes.rxdata")
    $data_skills        = load_data("Data/Skills.rxdata")
    $data_items         = load_data("Data/Items.rxdata")
    $data_weapons       = load_data("Data/Weapons.rxdata")
    $data_armors        = load_data("Data/Armors.rxdata")
    $data_enemies       = load_data("Data/Enemies.rxdata")
    $data_troops        = load_data("Data/Troops.rxdata")
    $data_states        = load_data("Data/States.rxdata")
    $data_animations    = load_data("Data/Animations.rxdata")
    $data_tilesets      = load_data("Data/Tilesets.rxdata")
    $data_common_events = load_data("Data/CommonEvents.rxdata")
    $data_system        = load_data("Data/System.rxdata")
    $data_mapinfos      = load_data("Data/MapInfos.rxdata")
    # システムオブジェクトを作成
    $game_system = Game_System.new
    # タイトルグラフィックを作成
    @sprite = Sprite.new
    @sprite.bitmap = RPG::Cache.title($data_system.title_name)
    # コマンドウィンドウを作成
    s1 = "NEW GAME"
    s2 = "LOAD GAME"
    s3 = "END GAME"
    @command_window = Window_Command.new(192, [s1, s2, s3])
    @command_window.back_opacity = 160
    @command_window.x = 320 - @command_window.width / 2
    @command_window.y = 288
    # コンティニュー有効判定
    # セーブファイルがひとつでも存在するかどうかを調べる
    # 有効なら @continue_enabled を true、無効なら false にする
    @continue_enabled = false
    for i in 0..3
      if FileTest.exist?("Save#{i+1}.rxdata")
        @continue_enabled = true
      end
    end
    # コンティニューが有効な場合、カーソルをコンティニューに合わせる
    # 無効な場合、コンティニューの文字をグレー表示にする
    if @continue_enabled
      @command_window.index = 1
    else
      @command_window.disable_item(1)
    end
    # タイトル BGM を演奏
    $game_system.bgm_play($data_system.title_bgm)
    # ME、BGS の演奏を停止
    Audio.me_stop
    Audio.bgs_stop
    # トランジション実行
    Graphics.transition
    # メインループ
    loop do
      # ゲーム画面を更新
      Graphics.update
      # 入力情報を更新
      Input.update
      # フレーム更新
      update
      # 画面が切り替わったらループを中断
      if $scene != self
        break
      end
    end
    # トランジション準備
    Graphics.freeze
    # コマンドウィンドウを解放
    @command_window.dispose
    # タイトルグラフィックを解放
    @sprite.bitmap.dispose
    @sprite.dispose
  end
end

class Window_BattleResult < Window_Base
  #--------------------------------------------------------------------------
  # ● 再定義 リフレッシュ
  #--------------------------------------------------------------------------
  def refresh
    self.contents.clear
    x = 4
    self.contents.font.color = system_color
    self.contents.draw_text(x, 0, 192, 32, "[Items]")
    y = 32
    for item in @treasures
      draw_item_name(item, 4, y)
      y += 32
    end
end
end

class Scene_Map
  #--------------------------------------------------------------------------
  # ● メニューの呼び出し
  #--------------------------------------------------------------------------
  def call_menu
    # メニュー呼び出しフラグをクリア
    $game_temp.menu_calling = false
    # メニュー SE 演奏フラグがセットされている場合
    if $game_temp.menu_beep
      # 決定 SE を演奏
      $game_system.se_play($data_system.decision_se)
      # メニュー SE 演奏フラグをクリア
      $game_temp.menu_beep = false
    end
  # メニュー呼び出し時 [0004]AG位置の判定スイッチをOFFにする
    $game_switches[0004] = false
  # メニュー呼び出し時 [0156]EV位置の判定スイッチをOFFにする
    $game_switches[0156] = false
  # プレイヤーの姿勢を矯正
    $game_player.straighten
    # メニュー画面に切り替え
    $scene = Scene_Menu.new
  end
end
