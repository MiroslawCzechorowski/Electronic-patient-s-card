<?php 
	if($_SERVER['REQUEST_METHOD']=='POST'){
		$id = $_POST['id'];
		$name = $_POST['name'];
		$lastname = $_POST['lastname'];
		$history = $_POST['history'];
		
		require_once('dbConnect.php');
		
		$sql = "UPDATE patients SET name = '$name', lastname = '$lastname', history = '$history' WHERE id = $id;";
		
		if(mysqli_query($con,$sql)){
			echo 'Patient Updated Successfully';
		}else{
			echo 'Could Not Update Patient Try Again';
		}
		
		mysqli_close($con);
	}