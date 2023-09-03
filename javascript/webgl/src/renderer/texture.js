exports.texture = ((gl, textureId, activeChannel)=>{

    function bind(){
        gl.bindTexture(gl.TEXTURE_2D, textureId);
        gl.activeTexture(gl.TEXTURE0+activeChannel);
    }

    function unbind(){
        gl.bindTexture(gl.TEXTURE_2D, null);
    }

    return {
        id : textureId,
        activeChannel,
        bind,
        unbind
    }

});