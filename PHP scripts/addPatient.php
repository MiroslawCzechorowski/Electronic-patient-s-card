<?php 
	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		//Getting values
		$name = $_POST['name'];
		$lastname = $_POST['lastname'];
		$history = $_POST['history'];
		$id= $_POST['id'];
		
		//Creating an sql query
		$sql = "INSERT INTO patients (id,name,lastname,history,images) VALUES ('$id','$name','$lastname','$history','NULL')";
		
		//Importing our db connection script
		require_once('dbConnect.php');
		
		//Executing query to database
		if(mysqli_query($con,$sql)){
			echo 'Patient Added Successfully';
		}else{
			echo 'Could Not Add Patient';
		}
		
		//Closing the database 
		mysqli_close($con);
	}