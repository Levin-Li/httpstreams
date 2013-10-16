<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!doctype html>
<html>
<head>
    
    <title>Embedded cuepoints : Flowplayer</title>

    <link rel="shortcut icon" href="http://flash.flowplayer.org/favicon.ico">
    <!-- standalone page styling. can be removed -->
    <style>
        body{
            width:982px;
            margin:50px auto;
            font-family:sans-serif;
        }
        a:active {
            outline:none;
        }
        :focus { -moz-outline-style:none; }

        .palert {
            padding: 12px;
            color: black;
            background-color: #ededed;
            box-shadow: none;
        }
    </style>

    
    
    <!-- flowplayer javascript component -->
    <script src="http://releases.flowplayer.org/js/flowplayer-3.2.12.min.js"></script>

    </head>

<body>
    <style>
/* container has a background image */

a.player {
    display:block;
    width:500px;
    height:340px;
    text-align:center;
    color:#fff;
    text-decoration:none;
    cursor:pointer;
    background:#000 url(/media/img/global/gradient/h500.png) repeat-x 0 0;
    background:-moz-linear-gradient(top,
                                    rgba(55, 102, 152, 0.9),
                                    rgba(6, 6, 6, 0.9));
    -moz-box-shadow:0 0 40px rgba(100, 118, 173, 0.5);
}

a.player:hover {
    background:-moz-linear-gradient(center top,
                                    rgba(73, 122, 173, 0.898),
                                    rgba(6, 6, 6, 0.898));
}

/* splash image */
a.player img {
    margin-top:125px;
    border:0;
}
#info {
    width:410px;
    padding:8px;
    border:1px solid #ccc;
    color: #333;
    background-color:#efefef;
    font:16px "bitstream vera sans", "verdana";
    margin-left:125px;
}
</style>


<a id="player" class="player"></a>


<div id="info" class="box info" style="color:#333">
    Please wait for 10 seconds and you'll see our embedded cuepoints in action.
</div>

<!-- this script block will install Flowplayer inside previous A tag -->
<script>
var info = document.getElementById("info");
flowplayer("player", "http://releases.flowplayer.org/swf/flowplayer-3.2.16.swf", {
    plugins: {
        pseudo: {
            url: "http://releases.flowplayer.org/swf/flowplayer.pseudostreaming-3.2.12.swf"
        }
    },
    
    clip: {
        url: 'demo/test.flv?r=' + Math.random(),
        start:60 * 24,
        provider: 'pseudo',

        // you can do different things on each cuepoint by checking the time
        onCuepoint: function(clip, p) {
            info.innerHTML = "time : " +p.time+ ", name: " + p.name;
        }
    }

});
</script>
</body>

</html>
