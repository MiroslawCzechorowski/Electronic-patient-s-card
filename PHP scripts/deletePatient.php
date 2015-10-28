<?php 
	$id = $_GET['id'];
	
	require_once('dbConnect.php');
	
	$sql = "DELETE FROM patients WHERE id=$id;";
	
	if(mysqli_query($con,$sql)){
		echo 'Patient Deleted Successfully';
	}else{
		echo 'Could Not Delete Patient Try Again';
	}
	
	mysqli_close($con);
