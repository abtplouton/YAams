require 'java'

include_class 'com.ezware.dialog.task.TaskDialogs'
include_class 'de.yaams.core.helper.Log'
include_class 'de.yaams.core.helper.gui.YDialog'
  
for s in ['Yrgss','Rect','YInput','Viewport','Tone','Audio','YGraphics','Color','Font','Sprite','Bitmap']
  include_class 'de.yaams.rgssplayer.core.java.'+s
end

#set basics
Dir.chdir(Yrgss.game.getPath.getAbsolutePath)
$debug = Yrgss.debug