<?php
 if($_SERVER['REQUEST_METHOD']=='POST'){
	$image = $_POST['image'];
 	$patientID=$_GET['id'];
	require_once('dbConnect.php');
 
	$sql ="SELECT id FROM images ORDER BY id ASC";
 
	$res = mysqli_query($con,$sql);
 
	$id = 0;
 
	while($row = mysqli_fetch_array($res)){
	$id = $row['id'];
 }
 
 $path = "uploads/$id.png";
 
 $actualpath = "http://serwer1558258.home.pl/$path";
 
 $sql = "INSERT INTO images  (image, patientID) VALUES ('$actualpath','$patientID')";
 
 if(mysqli_query($con,$sql)){
 file_put_contents($path,base64_decode($image));
 echo "Successfully Uploaded";
 }
 
 mysqli_close($con);
 }else{
 echo "Error";
 }
 ?>