<?php
    $con = mysqli_connect("localhost", "tracking", "system", "tracking");
    mysqli_query($con,'SET NAMES utf8');

    $DeviceID = $_POST["DeviceID"];

    $response = array();

	$count_statement = mysqli_query($con, "Select Distinct DeviceID FROM mapping WHERE DeviceID = '$DeviceID'");
	$count = mysqli_num_rows($count_statement);

	if($count >= 1){
		$NFS_statement = mysqli_query($con, "Select Distinct FileID FROM tracking WHERE DeviceID = '$DeviceID'");
		while($row=mysqli_fetch_assoc($NFS_statement)){
			$command = $row["FileID"];
			system("python Socket.py /home/tracking/trace/Deletion '$command'");
			system("/home/tracking/trace/Deletion '$command'");
			
			#removed 테이블에 저장
			$statement = mysqli_prepare($con, "INSERT INTO removed values ('$DeviceID','$ServiceID','$command')");
			mysqli_stmt_bind_param($statement, "sss", $Date, $DeviceID, $ServiceID);
			mysqli_stmt_execute($statement);
		}

		#맵핑정보만 
    		#$statement = mysqli_query($con, "DELETE FROM tracking WHERE DeviceID = '$DeviceID'");
		$statement = mysqli_query($con, "DELETE FROM mapping WHERE DeviceID = '$DeviceID'");

		$response["success"] = true;
	}
	
    else{
	$response["success"] = false;
    }

    echo json_encode($response);

    mysqli_close($con);


?>
