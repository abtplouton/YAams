include_class 'de.yaams.extensions.rgssproject.database.RGSS1Load'

require 'fileutils'

#backup old data
if (File.exist?(RGSS1Load.sFile))
  if (File.exist?(RGSS1Load.sFile+".bac"))
    File.delete(RGSS1Load.sFile+".bac")
  end
  FileUtils.mv(RGSS1Load.sFile,RGSS1Load.sFile+".bac")
end

#save data
save_dataE(RGSS1Load.sSave,RGSS1Load.sFile);