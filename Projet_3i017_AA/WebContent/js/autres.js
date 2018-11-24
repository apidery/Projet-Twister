/*
//8.3 
//from id à -1 
/*

min et max permet d'afficher les 10 plus récents par exemple 
minId = message le plus ancien



//A la suite d'une publication on veut rafraichir avec le nouveau message en haut de la listMessage

function refreshMessage(){
 if(env.query!=undefined){
     return;
 }
 if(!noConnection){
     $.ajax({
         type : "GET",
         url : "message/GetMessages",
         data:"key="+env.key+"&query=''&from="+env.fromId+"&id_max=-1&id_min="+env.maxId+"&nb=1",
         datatype : "json",
         succes : function(rep){
             refreshMessageReponse(rep);
         },
         error : function (jqXHR, textStatus, errorT){
             alert(textStatus);
         },
     });  
 }
}

function refreshMessageReponse(rep){
 
 var tab = JSON.parse(rep, revival);

 if(tab.length != 0){
     for(var i=tab.length-1; i>=0; i--){
         var m = tab[i];
         $("#messages").prepend(m.getHTML());
         env.msgs[m.id] = m;

         if(m.id > env.maxId){
             env.maxId = m.id;
         }

         if (m.id < env.minId) {
             env.minId = m.id;
         }
     }
 }
 else {
     console.log(rep.message + ", ERROR_CODE: " + rep.errorcode);
     func_erreur(rep.message);
 }   
}



// TD - Map Reduce 

// Question 1 :

function map(){
    var text = this.text;
    var words = text.match("/\w+/g");
    var tf = {};
    for(var i=0; i< words.length; i++){
        if(tf[words[i]] == null){
            tf[words[i]] = 1;
        }else{
            tf[words[i]] += 1;
        }
    }

    for(w in tf){
        enit(w,1);
    }
}

function reduce(key, walues){
    return values.length;
}

// Question 2 :

" Jean fait un projet"
    "Jean mange un sandwich" 

{"jean" : [(0,0) , (1,0)]} 
 "fait" : [(0,1)]; 
 "un" : [(0,2)(0,2)]
    .
    .
    .

m = function {
    var text = this.text;
    var id = this.id;
    var words = text.match ("/\w+/g");
    var tf = {};
    for(var i=0; words.length; i++){
        if(tf[words[i]] == null){
            tf[words[i]] +=1;
        }else{
            enit(tf[words[i]] +=1);
        }
    }

    for(w in tf){
        var ret = {};
        ret[id] = tf[w];
        enit(w,ret);
    }
}

r = function (key, values){
    var ret = {};
    for(var i=0; i<values.length; i++){
        for(var d in values[i]){
            ret[d] = values[i][d];
        }
    }

    return ret;
}

f = function(k,v){
    var df = Object.keys(v);
    
    for(d in v){
        v[d] = v[d]*Math.log(N,df);
    }

    return v;
}

db.runCommand({
    mapreduce : " x", map : 
})*/