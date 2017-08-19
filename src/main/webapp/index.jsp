<%@ page contentType="text/html; charset=utf-8"%>

<html>
<body>
<h2>springMvc文件上传</h2>
<form name="form1" action="/manage/product/upload" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <input type="submit" value="springmvc上传文件" />
</form>
</body>
</html>
