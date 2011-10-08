require 'zlib'
require 'ftools'

#include_class 'de.yaams.extensions.script.ScriptTab'
#include_class 'de.yaams.extensions.script.ScriptAction'

#load scripts
data = load_dataE(ScriptTab.scriptFile);

#fill ary
script = [];

#convert
for s in ScriptTab.scripts
  #name = rb[1];
  script << [s.getNumber,s.getTitle,Zlib::Deflate.deflate(s.getContent)]
end

save_dataE(script,ScriptTab.scriptFile);