<?php

//session_start();

$url = 'http://10.14.9.4:9000/';  //http://10.14.6.60:9000/

$postdata = file_get_contents("php://input");

$postdata = json_decode($postdata, true);

if (array_key_exists('path', $postdata)) {
	$path = $postdata['path'];
	unset($postdata['path']);
}

$postdata1 = json_encode($postdata);
//print_r($postdata1);
//print_r($path->path);
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url.$path);
curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json'));
curl_setopt($ch, CURLOPT_POST, 1);
curl_setopt($ch, CURLOPT_POSTFIELDS,$postdata1);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$response  = curl_exec($ch);

print_r($response);

curl_close($ch);

?>