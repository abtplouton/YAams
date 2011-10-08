include_class 'de.yaams.extensions.rgssproject.map.rxdata.RXDataWriter'

#load map
map = RXDataWriter.rmap;
data = RXDataWriter.data;

map.width = data[0].length
map.height = data[0][0].length
map.data = Table.new(map.width,map.height,3)

#modif
for z in 0...3
  for x in 0...data[0].length
    for y in 0...data[0][0].length
      map.data[x,y,z]=data[z][x][y];
    end
  end
end