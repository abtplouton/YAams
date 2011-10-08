
# Loads the data file indicated by filename and restores the object.
def load_data(filename)
  Log.ger.debug('Read '+filename)
  File.open(File.join(Yrgss.game.getPath.getAbsolutePath,filename), "rb") { |f|
    obj = Marshal.load(f)
  }
end

# Saves the object obj to the data file indicated by filename.
def save_data(obj, filename) 
  File.open(File.join(Yrgss.game.getPath.getAbsolutePath,filename), "wb") { |f|
    Marshal.dump(obj, f)
  }
end

# Print something in a Dialog window
def print(*args)
  YDialog.ok(Yrgss.game.getName,args.inspect.to_s);
end

# Print something in a Dialog window
def p(*args)
  YDialog.ok(Yrgss.game.getName,args.inspect.to_s);
end