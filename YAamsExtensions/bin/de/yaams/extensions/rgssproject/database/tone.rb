class Tone
  def initialize(r, g, b, a = 0)
     @red = r
     @green = g
     @blue = b
     @gray = a
  end
  def set(r, g, b, a = 0)
     @red = r
     @green = g
     @blue = b
     @gray = a
  end
  def color
     Color.new(@red, @green, @blue, @gray)
  end
  def _dump(d = 0)
     [@red, @green, @blue, @gray].pack('d4')
  end
  def self._load(s)
     Tone.new(*s.unpack('d4'))
  end
  attr_accessor(:red, :green, :blue, :gray)
end