<?php
	if($_SERVER['REQUEST_METHOD']=='GET'){
	require_once('dbConnect.php');
	$id = $_GET['id'];
	$sql = "select image from images where patientID = '$id'";
	
	$res = mysqli_query($con,$sql);
	
	$result = array();
	
	while($row = mysqli_fetch_array($res)){
		array_push($result,array('url'=>$row['image']));
	}
	
	echo json_encode(array("result"=>$result));
	
	mysqli_close($con);
}else{
		echo "Error";
	}
?>