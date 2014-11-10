<?php

require 'vendor/autoload.php';
 
use Parse\ParseClient;
use Parse\ParseObject;
use Parse\ParseQuery;


 
ParseClient::initialize('QXRTROGsVaRn4a3kw4gaFnHGNOsZxXoZ8ULxwZmf', 'BCJuFgG7GVxZfnc2mVbt2dzLz4bP7qAu16xaItXB', 'j9TIxQX3zEHkDPfQszCa6ariYTmZ8JU0RC31BKZK');

echo "this is the speecheck.php file";

function checkSpeed($goal){
  $justStarted = true;

  $query = new ParseQuery("Speed");
  $query->descending("createdAt");
  $latestObj = $query->first();
  $latestSpeed = $latestObj->get("speed");
  $latestStatusObj = $latestObj->get("status");

  while(latestStatusObj == "running"){

    if($latestSpeed >= $goal){
      $justStarted = false;
    }

    if($justStarted = false && $latestSpeed){
      if ($latestSpeed <= $goal){
        exec("php motivate_call.php");
      }
    }

  $latestStatusObj = $statusQuery->first();
  }


}
?>

