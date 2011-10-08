
class Color
  def initialize(r, g, b, a = 255)
     @red = r
     @green = g
     @blue = b
     @alpha = a
  end
  def set(r, g, b, a = 255)
     @red = r
     @green = g
     @blue = b
     @alpha = a
  end
  def color
     Color.new(@red, @green, @blue, @alpha)
  end
  def _dump(d = 0)
     [@red, @green, @blue, @alpha].pack('d4')
  end
  def self._load(s)
     Color.new(*s.unpack('d4'))
  end
  attr_accessor(:red, :green, :blue, :alpha)
end