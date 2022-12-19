/*FUNCIONES GENERALES*/
function log(variable){
    console.log(variable);
}

function ajax(curl, datos, callback){
    $.ajax({
        url: curl,
        data: datos,
        method: "post",
        cache: false, 
        success: callback
    });
}

function ajaxFormData(curl, formData, callback){
    $.ajax({
        url: curl,
        type: "POST",
        data: formData,
        contentType: false,
        processData: false,
        cache: false,
        success: callback
    });
}

function valoresFormulario(idFormulario){    
    var $inputs = $('#'+idFormulario+' :input[name]');
    
    var arrayValores = {};
    $inputs.each(function() {
        if(this.name.indexOf('[]') !== -1){//si es array
            if($(this).is(':checkbox') && !$(this).is(':checked')){//si es checkbox y no esta seleccionado, saltar
                return true;
            }

            nombreSimple = this.name.replace('[]','');
            if(typeof arrayValores[nombreSimple] === 'undefined') {//sino exsiste creo el array dentro
                arrayValores[nombreSimple] = [];
            }
            arrayValores[nombreSimple].push($(this).val());
        }else{//sino es array ingresa normal
            if(($(this).is(':radio') || $(this).is(':checkbox')) && !$(this).is(':checked')){//si es radio o checkbox simple y no esta seleccionado, saltar
                return true;
            }
            arrayValores[this.name] = $(this).val();
        }        
    });

    return arrayValores;
}

var segundosMsj = 5;

function msjExito(id, msj){
    $('#'+id).html(msj);
    $('#'+id).attr('class', 'alert alert-success');
    $('#'+id).attr('role', 'alert');
    $('#'+id).slideDown();
    limpiarMsj(id);
}

function msjError(id, msj){
    $('#'+id).html(msj);
    $('#'+id).attr('class', 'alert alert-danger');
    $('#'+id).attr('role', 'alert');
    $('#'+id).slideDown();
    limpiarMsj(id);   
}

function limpiarMsj(id){
    setTimeout(function(){ 
        $('#'+id).html('');
        $('#'+id).slideUp();
    }, 1000 * segundosMsj);

}

function validarCampo(id, requerido, formato) {
    var obj = byId(id);
    var valor = obj.value;
    var valido = true;
    var $objValidar = $(obj).closest(".validar");//inputs normales
    //var $objValidar = $(obj);//inputs normales

    /*if ($(obj).is("select")) {
        $objValidar = $(obj).prev(".select-dropdown").prev(".select-dropdown");//para selects por Materialize eq(1) = segundo padre
    }*/

    if(valor.length > 0 && valor !== null && valor !== 'null'){
        if ((formato === 'lista' && (valor === '-1' || valor === '0')) 
        || (formato === 'listaExceptCero' && valor === '-1')
        || (formato === 'cantidad' && (parseFloat(valor) <= 0 || isNaN(parseFloat(valor)) === true))
        || (formato === 'monto' && (parseFloat(valor) <= 0 || isNaN(parseFloat(valor)) === true))
        || (formato === 'numPositivo' && (parseFloat(valor) < 0 || isNaN(parseFloat(valor)) === true))) {
            $objValidar.addClass('is-invalid').removeClass('is-valid');
            valido = false;
        } else {
            $objValidar.addClass('is-valid').removeClass('is-invalid');
            valido = true;
        }
    }
    
    if(requerido && valor.length === 0){
        $objValidar.addClass('is-invalid').removeClass('is-valid');
        valido = false;
    }
    
    return valido;
}

function validarEmail(email) {
    var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}

function limpiarInputs(){
    $('.is-valid').removeClass('is-valid');
    $('.is-invalid').removeClass('is-invalid');
}

function nullPorVacio(valor){
    if(valor === null){
        valor = '';
    }
    return valor;
}

function isValidDate(valor) {
    if(!(valor instanceof Date)){
        if(valor.indexOf('-')){
            valor = valor.split('-').reverse().join('-');
        }
        valor = new Date(valor);
    }
    return valor instanceof Date && !isNaN(valor);
}

function enter(event){
    if (event.keyCode === 13) {
        return true;
    }
}

function numeros(e){
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 0 || tecla == 8){//8 = delete
        return true;
    }else if(tecla == 13){
        return false;
    }else{
        patron = /[0-9]/;
        te = String.fromCharCode(tecla);
        return patron.test(te);
    }
}

function numerosLetras(e){
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 0 || tecla == 8){//8 = delete
        return true;
    }else if(tecla == 13){
        return false;
    }else{
        patron = /[0-9A-Za-zÑñ\s ]/;
        te = String.fromCharCode(tecla);
        return patron.test(te);
    }
}

function numerosLetrasSinEspacio(e){
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 0 || tecla == 8){//8 = delete
        return true;
    }else if(tecla == 13 || tecla == 32){
        return false;
    }else{
        patron = /[0-9A-Za-zÑñ\s ]/;
        te = String.fromCharCode(tecla);
        return patron.test(te);
    }
}

function letrasSinEspacio(e){
    tecla = (document.all) ? e.keyCode : e.which;
    
    if (tecla == 0 || tecla == 8){//8 = delete
        return true;
    }else if(tecla == 13 || tecla == 32){
        return false;
    }else{
        patron = /[A-Za-zÑñ\s]/;
        te = String.fromCharCode(tecla);
        return patron.test(te);
    }
}

function letras(e){
    tecla = (document.all) ? e.keyCode : e.which;
    
    if (tecla == 0 || tecla == 8){//8 = delete
        return true;
    }else if(tecla == 13){
        return false;
    }else{
        patron = /[A-Za-zÑñ\s ]/;
        te = String.fromCharCode(tecla);
        return patron.test(te);
    }
}

// GENERAR ID UNICO
function guid() {
    function s4() {
      return Math.floor((1 + Math.random()) * 0x10000)
        .toString(16)
        .substring(1);
    }
    return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
      s4() + '-' + s4() + s4() + s4();
}

function byId(id){
    if (document.getElementById(id) === null) {
        alert('El elemento con id ' + id + ' no existe.');
    }
    return document.getElementById(id);
}

function notificacion(/*titulo, */mensaje, tipo){
    PNotify.prototype.options.styling = "fontawesome";
      
    new PNotify({
      /*title: titulo,*/
      text: mensaje,
      type: tipo,
      buttons: {
        closer: true,
        sticker: true
      }
    });
}

function previsualizarImagen(objInput, idObjetivo, usarBackground){
    var arrayArchivos = objInput.files;
    var ventana = window.URL || window.webkitURL;
    var ok = false;

    $.each(arrayArchivos, function(indice, objArchivo){
    	if(objArchivo.type.indexOf('image') >= 0){
            var url = ventana.createObjectURL(objArchivo);

            if(usarBackground === 1){
                $("#"+idObjetivo).css('background-image', 'url(' + url + ')');
            }else{
                $("#"+idObjetivo).attr("src", url);
            }           
            window.URL.revokeObjectURL(objArchivo);
            ok = true;
        }else{
            $(objInput).val('');
            alert('El archivo no es una imagen');
            ok = false;
        }
    });
    return ok;
}

function redirigir(url, segundos){
    setTimeout(function(){
        document.location.href = url;
    }, 1000 * segundos);
}

function cargarFunciones(){
    $(".mascaraFecha").mask(config.MASCARA_FECHA_JS,{placeholder:" "});

    $('.usaDatePicker').datepicker({
        language: 'es',
        //format: 'mm/dd/yyyy'
        //format: 'dd-mm-yyyy',
        format: config.FORMATO_FECHA_JS,
        forceParse: false
    });

    $('.usaSelect2').select2({
        theme: "bootstrap",
        width: '100%'
    });
}

/**
* @param input String Numero a evaluar (obligatorio) Formatos sin comma: 1234 o 1234.00 o 1234.56
* @param decimales String Cantidad decimales a usar (opcional) por defecto 2
* @param simbolo String Simbolo de la moneda (opcional) Bs. BsS. $ €
*
* @return String Regresa el mismo número con formato 1,234.57
*/
function formatoNumero(input, decimales, simbolo) {
	var num = input;
	var decimales = (typeof(decimales) === 'undefined') ? 2 : decimales;
	var simbolo = (typeof(simbolo) === 'undefined') ? '' : ' '+simbolo;
	
	// todo numero debe ser flotante, si introduce letras devuelve string ""
	num = parseFloat(num).toFixed(decimales);
	if (isNaN(num)) {
		return "";
	}
	
	//Agrega comma cada 3 digitos a la izquierda
	var splitStr = num.toString().split('.');
	var splitLeft = splitStr[0];
	var splitRight = splitStr.length > 1 ? ',' + splitStr[1] : '';
	var regx = /(\d+)(\d{3})/;
	
	while (regx.test(splitLeft)) {
		splitLeft = splitLeft.replace(regx, '$1' + '.' + '$2');
	}
	
	return splitLeft + splitRight + simbolo;		
}

/**
* @param input String Numero a evaluar 1000 o 1500,57 o 1.234,57
*
* @return String Regresa el numero sin formato 1234.57
*/
function limpiarFormato(input){
	if (typeof(input) === 'undefined' || input === ""){
		return "";
	}			
	var num = input.toString().replace(/\./g, '').replace(/,/g, '.');
	return parseFloat(num);
}

/**
* @param e Event Es la captura del evento al escribir
* @param inputValue String Es el valor del input sin inluir tecla presionada actual
*
* @return Boolean True permite escritura, False no lo permite
*/
function numerosComma(e, inputValue){
	tecla = (document.all) ? e.keyCode : e.which;
	if (tecla == 0 || tecla == 8)
		return true;
	patron = /[0-9,]/;
	te = String.fromCharCode(tecla);
	// si ya existe coma en el input y si esta escribiendo coma otra vez
	if (inputValue.indexOf(',') > -1 && te === ','){
		return false;
	}			
	return patron.test(te);
}