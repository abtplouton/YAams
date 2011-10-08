require 'java'

include_class 'com.ezware.dialog.task.TaskDialogs'
include_class 'de.yaams.maker.helper.Log'
  
# Loads the data file indicated by filename and restores the object.
def load_dataE(filename)
  Log.ger.debug('Read '+filename)
  File.open(filename, "rb") { |f|
    obj = Marshal.load(f)
  }
end

# Saves the object obj to the data file indicated by filename.
def save_dataE(obj, filename) 
  File.open(filename, "wb") { |f|
    Marshal.dump(obj, f)
  }
end