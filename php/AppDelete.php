<?php
    $con = mysqli_connect("localhost", "tracking", "system", "tracking");
    mysqli_query($con,'SET NAMES utf8');

    $DeviceID = $_POST["DeviceID"];
    $ServiceID = $_POST["ServiceID"];

	$count_statement = mysqli_query($con, "Select Distinct ServiceID FROM mapping WHERE DeviceID = '$DeviceID'");
	$count = mysqli_num_rows($count_statement);

	$response = array();

	if($count >= 1){
		$NFS_statement = mysqli_query($con, "Select Distinct FileID FROM tracking WHERE DeviceID = '$DeviceID' AND ServiceID = '$ServiceID'");
		while($row=mysqli_fetch_assoc($NFS_statement)){
			$command = $row["FileID"];
			system("python Socket.py /home/tracking/trace/Deletion '$command'");
			system("/home/tracking/trace/Deletion '$command'");
			
			#removed 테이블에 
			$statement = mysqli_prepare($con, "INSERT INTO removed values ('$DeviceID','$ServiceID','$command')");
			mysqli_stmt_bind_param($statement, "sss", $Date, $DeviceID, $ServiceID);
			mysqli_stmt_execute($statement);
		}

		#맵핑만 
		#$statement = mysqli_query($con, "DELETE FROM tracking WHERE DeviceID = '$DeviceID' AND ServiceID = '$ServiceID'");
		$statement = mysqli_query($con, "DELETE FROM mapping WHERE DeviceID = '$DeviceID' AND ServiceID = '$ServiceID'");

		$response["success"] = true;
	}

	else{
		$response["success"] = false;
	}

    echo json_encode($response);

    mysqli_close($con);
?>
