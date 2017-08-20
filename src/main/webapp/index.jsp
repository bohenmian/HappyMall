<%@ page contentType="text/html; charset=utf-8"%>

<html>
<body>
<h2>springMvc文件上传</h2>
<form name="form1" action="/manage/product/uploadFile" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <input type="submit" value="springmvc上传文件" />
</form>

<h2>富文本图片上传</h2>
<form name="form1" action="/manage/product/uploadText" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <input type="submit" value="富文本图片上传" />
</form>
</body>
</html>
