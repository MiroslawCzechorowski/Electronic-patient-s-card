<?php
$con=mysqli_connect("serwer1558258.home.pl","18744391_0000001","LajtowaSesja123","18744391_0000001") or die('Unable to Connect');
$login = $_POST['login'];
$password = $_POST['password'];
$sql = "select * from users where nazwa='$login' and haslo='$password'";
$res = mysqli_query($con,$sql);
$check = mysqli_fetch_array($res);
if(isset($check)){
echo 'success';
}else{
echo 'failure';
}
mysqli_close($con);
?>