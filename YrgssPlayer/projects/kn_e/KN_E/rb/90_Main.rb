#==============================================================================
# ■ Main
#------------------------------------------------------------------------------
# 　各クラスの定義が終わった後、ここから実際の処理が始まります。
#==============================================================================

begin
  # トランジション準備
  Graphics.freeze
  # シーンオブジェクト (タイトル画面) を作成
  
  $scene = Scene_Title.new
  # $scene が有効な限り main メソッドを呼び出す
  while $scene != nil
    $scene.main
  end
  # フェードアウト
  Graphics.transition(20)
rescue Errno::ENOENT
  # 例外 Errno::ENOENT を補足
  # ファイルがオープンできなかった場合、メッセージを表示して終了する
  filename = $!.message.sub("No such file or directory - ", "")
  print("File #{filename} not found")
end
