require 'zlib'
require 'ftools'

include_class 'de.yaams.extensions.rgssproject.script.ScriptTab'
include_class 'de.yaams.extensions.rgssproject.script.ScriptAction'

#load scripts
data = load_dataE(ScriptTab.scriptFile);

#fill ary
script = [];

#convert and eval
for rb in data
  #name = rb[1];
  script << ScriptAction.new(rb[1],Zlib::Inflate.inflate(rb[2]),rb[0])
end

return script;