<?php
    $con = mysqli_connect("localhost", "tracking", "system", "tracking");
    mysqli_query($con,'SET NAMES utf8');

    $Date = $_POST["Date"];
    $DeviceID = $_POST["DeviceID"];
    $ServiceID = $_POST["ServiceID"];

    $count_statement = mysqli_query($con, "Select Distinct FileID FROM tracking WHERE Date = '$Date' AND DeviceID = '$DeviceID' AND ServiceID = '$ServiceID'");
    $count = mysqli_num_rows($count_statement);

    $response = array();

    if($count >= 1){
	$NFS_statement = mysqli_query($con, "Select Distinct FileID FROM tracking WHERE Date = '$Date' AND DeviceID = '$DeviceID' AND ServiceID = '$ServiceID'");
	while($row=mysqli_fetch_assoc($NFS_statement)){
		$command = $row["FileID"];
		system("python /home/tracking/trace/Socket.py '$command'");
		system("/home/tracking/trace/Deletion '$command'");
		
		$statement = mysqli_prepare($con, "INSERT INTO removed values ('$DeviceID','$ServiceID','$command')");
		mysqli_stmt_bind_param($statement, "sss", $Date, $DeviceID, $ServiceID);
		mysqli_stmt_execute($statement);
   	}

	    #로그 삭제
	#$statement = mysqli_prepare($con, "DELETE FROM tracking WHERE ( Date= ? AND DeviceID = ? AND ServiceID = ?)");
	#mysqli_stmt_bind_param($statement, "sss", $Date, $DeviceID, $ServiceID);
	#mysqli_stmt_execute($statement);

	$response["success"] = true;
    }

    else{
	$response["success"] = false;
    }

    echo json_encode($response);

    mysqli_close($con);

?>
