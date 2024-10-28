let localVideo = document.getElementById("local-video")
let remoteVideo = document.getElementById("remote-video")

localVideo.style.opacity = 0
remoteVideo.style.opacity = 0

localVideo.onplaying = () => {localVideo.style.opacity = 1}
remoteVideo.onplaying = () => {remoteVideo.style.opacity = 1}

let peer
function init(userId){
    peer = new peer(userId,{
        host: '255.255.255.0',
        port: 9000,
        path: '/'
    })

    peer.on('open',() => {
        // WE WILL MAKE A CALL TO A FUNCTION TO THE KOTLIN  IN ANDROID
        Android.onPeerConnected()
    })

    listen()
}

let localStream

function listrn(){
    peer.on('call',(call) => {

       

       navigator.getUserMedia({
        audio: true,
        video: true
       }, (stream) => {

        localVideo.srcObject = stream
        localStream = stream

        call.answer(stream)
        call.on('stream', (remoteStream) =>{
            remoteVideo.srcObject = remoteStream

            remoteVideo.className = "primary-video"
            localVideo.className = "secondary-video"
        })
       
       })
       
    })
}

function startCall(otherUserId){

    navigator.getUserMedia({
        audio: true,
        video: true
    }, (stream) =>{
        localVideo.srcObject = stream
        localStream = stream

        const call = peer.call(otherUserId,stream)

        call.on('stream',(remoteStream)=>{
            remoteStream.srcObject = remoteStream

            remoteVideo.className = "primary-video"
            localVideo.className = "secondary-video"
        })
    })
} 

function toggleVideo(b){
    if(b == "true"){
        localStream.getVideoTracks()[0].enable = true
    }else{
        localStream.getVideoTracks()[0].enable = false
    }
}

function toggleAudio(b){
    if(b == "true"){
        localStream.getAudioTracks()[0].enable = true
    }else{
        localStream.getAudioTracks()[0].enable = false
    }
}