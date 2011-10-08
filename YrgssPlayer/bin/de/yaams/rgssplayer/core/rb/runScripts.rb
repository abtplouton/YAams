require 'zlib'
require 'ftools'

#load scripts
data = load_data(Yrgss.game.scriptPath);

Yrgss.loadingStep = 0;
Yrgss.loadingMax = data.size*2;

#names = []
#id=0 

#fill ary
code = [];

#convert and eval
for rb in data
  #name = rb[1];
  code << [rb[1],Zlib::Inflate.inflate(rb[2])] #.gsub(/\n/,"")+"#"
  Yrgss.loadingStep += 1;
  #name.delete!(" ")
  #name.delete!("*")
  #name.delete!("<")
  #name.delete!(">")
  #name.delete!("-")
  #name = Yrgss.tmpFolder.getAbsolutePath+'/rb/'+id.to_s+'_'+name+'.rb'
  #names << name;
  #File.open(name, 'w') do |f2|
  #begin
  #  eval(Zlib::Inflate.inflate(rb[2]).gsub(/\n/,"")+"#",nil,rb[1],1);
  #rescue
  #  p $!;
  #end
    #f2.write(Zlib::Inflate.inflate(rb[2]).gsub(/\n/,"")+"#")
  #end
  #id += 1
end

# save names for java
Yrgss.code = code
Yrgss.codeStep = -1;

#Yrgss.scriptNames = names