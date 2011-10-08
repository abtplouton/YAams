s = Sprite.new
s.bitmap = Bitmap.new('kachel.png');
s.bitmap.blt(0, 0, s.bitmap, Rect.new(20,0,20,20)); 
s.bitmap.stretch_blt(Rect.new(0,60,40,40), s.bitmap, Rect.new(60,40,20,20)); 
s.bitmap.font.color = Color.new(0,0,0)
s.bitmap.draw_text(0,0,20,20,'h')

g = Sprite.new
g.x = 100
g.y = 100
g.bitmap = Bitmap.new(40,40);
g.bitmap.clear
g.bitmap.stretch_blt(Rect.new(0,0,40,40), s.bitmap, Rect.new(60,60,40,40)); 
