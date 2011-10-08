include_class 'de.yaams.extensions.rgssproject.database.RGSS1Load'

require 'fileutils'

#backup old data
if (File.exist?(RGSS1Load.sFile+".bac"))
  if (File.exist?(RGSS1Load.sFile))
    File.delete(RGSS1Load.sFile)
  end
  FileUtils.mv(RGSS1Load.sFile+".bac",RGSS1Load.sFile)
end