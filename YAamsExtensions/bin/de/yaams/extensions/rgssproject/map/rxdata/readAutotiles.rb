include_class 'de.yaams.extensions.rgssproject.map.rxdata.RXDataReader'


#load data
data = load_dataE(RXDataReader.data);
RXDataReader.data = data[RXDataReader.data2].autotile_names;
 
#fill ary
#script = [];

#convert and eval
#for rb in data
#  #name = rb[1];
#  script << ScriptAction.new(rb[1],Zlib::Inflate.inflate(rb[2]),rb[0])
#end

return data[RXDataReader.data2];