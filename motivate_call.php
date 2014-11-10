<?php
// Include the Twilio PHP library
require 'twilio-php/Services/Twilio.php';
// Twilio REST API version
$version = "2010-04-01";
// Set our Account SID and AuthToken
$sid = 'AC6918ac7404fcfbb953976c3fe3a1a432';
$token = '9e027d2cffa89f41b75063e1ae9c03b7';
// A phone number you have previously validated with Twilio
$phonenumber = '2244124770';
// Instantiate a new Twilio Rest Client
$client = new Services_Twilio($sid, $token, $version);
try {
// Initiate a new outbound call
$call = $client->account->calls->create(
$phonenumber, // The number of the phone initiating the call
'8474019260', // The number of the phone receiving call
'http://demo.twilio.com/welcome/voice/' // The URL Twilio will request when the call is answered
);
echo 'Started call: ' . $call->sid;
} catch (Exception $e) {
echo 'Error: ' . $e->getMessage();
}