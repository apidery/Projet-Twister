
function connect(formulaire){
	var login = formulaire.login.value
	var mdp = formulaire.mdp.value
	var ok = verif_formulaire_connexion(login, mdp)

	if(ok){
		connecte(login,mdp);
	}
}


function enregistrement(formulaire){

	var login = formulaire.login.value;
	var mdp = formulaire.mdp.value;
	var mdp_confirm = formulaire.mdp_confirm.value;
	var nom = formulaire.nom.value;
	var prenom = formulaire.prenom.value;
	var mail = formulaire.email.value;
	var anniversaire = formulaire.anniversaire.value;

	var ok = verif_formulaire_enregistrement(login, mdp, mdp_confirm, prenom, nom, mail);

	if(ok){
		enregistre(prenom, nom, mail, login, mdp, anniversaire);
	}
}

function verif_formulaire_enregistrement(login, mdp, mdp_confirm, prenom, nom, email) {

    // Validité du login
	if (login.length < 5) {
		func_erreur("Login trop court. (5 caractères minimum.)");
		return false;
	} else if (login.length > 20) {
		func_erreur("Login trop long. (20 caractères maximum.)");
		return false;
	}

    // Validité du mot de passe
    if (mdp != mdp_confirm ) {
        func_erreur("Erreur de mot de passe. La deuxième saisie de mot de passe ne correspond pas.");
        return false;
    } else if (mdp.length < 8) {
        func_erreur("Le mot de passe doit posséder au moins 8 caractères.");
        return false;
    } else if (mdp.length > 64) {
        func_erreur("Le mot de passe ne doit pas contenir plus de 64 caratères.");
        return false;
    }

    // Validité du prénom
    if (prenom.length > 64) {
        func_erreur("Prenom trop long.");
        return false;
    }

    // Validité du nom
    if (nom.length > 64) {
        func_erreur("Nom trop long.");
        return false;
    }

    // Validité de l'adresse mail
    var re_email = new RegExp("[a-z]+[@][a-z]+[\.][a-z]+");
	if (email.length < 1) {
		func_erreur("Adresse email trop courte.");
		return false;
	} else if (email.length > 64) {
        func_erreur("Adresse email trop long.");
        return false;
    } else if (!re_email.exec(email)) {
		func_erreur("L'adresse email saisie n'est pas correcte.");
		return false;
	}

	return true;
}


function verif_formulaire_connexion(login, mdp){

	if(login.length == 0){
		func_erreur("Login obligatoire");
		return false;
	}

	if(mdp.length == 0){
		func_erreur("Mot de passe obligatoire");
		return false;
	}

	return true;
}


function func_erreur(msg){
	var message_box = "<div id=\" msg_error_connexion\">" +msg+"</div>"
	var old_message = $('#msg_error_connexion');
	
	if(old_message.length == 0){
		$("form").prepend(message_box); // ajoute message box en premier élément.
	}else{
		old_message.replaceWith(message_box);
	}

	$("msg_error_connexion").css({"color":"red", "margin-top":"10px"});
}



function makeEnregistrementPanel(){
	$("body").load("html/enregistrement.html");
}

function makeConnexionPanel(){
	$("body").load("html/connexion.html");
}


function connecte(login, password){
    if(!connection){
        $.ajax({
            type : "GET",
            url : "user/Login",
            data : "login="+ login +"&mdp="+password,
            dataType : "json",
            success: function (rep){
                reponseConnexion(rep);
            },
            error: function (jqXHR, textStatus, errorT){
                makeConnexionPanel();
            }
        });
    }
}

function enregistre(prenom, nom, mail, login, password, anniversaire){
    if(!connection){
        $.ajax({
            type : "GET",
            url : "user/CreateUser",
            data : "prenom="+prenom+"&nom="+nom+"&login="+login+"&mail="+mail+"&pwd="+password+"&datenaiss="+anniversaire,
            dataType : "JSON",
            success : function(rep){
                responseEnregistrement(rep);
            },
            error : function (jqXHR, textStatus, errorT){
                alert("Erreur lors de l'enregistrement.");
                makeEnregistrementPanel();
            }
        })
    }
}

function reponseConnexion(rep){
	if(rep.error == undefined){
		env.key = rep.key;
		env.id  = rep.id;
		env.login = rep.login;
		env.nom = rep.nom;
		env.prenom = rep.prenom;
		env.date_naiss = rep.date_naiss;
		env.follows = new Set();
		env.nb_abonne = rep.nbAbo;
		env.msgs = new Set();
		env.msgsProfil = new Set();
		env.nbMessagesTotal =  rep.nbMessagesTotal;
    	env.nbMessagesUser = rep.nbMessagesUser;
    	env.colorProfil = rep.color;

		connection = true;

		listFollower(env.login);
		
		makeMainPanel();

	}else{
		func_erreur("Erreur vérifier votre login et mot de passe.");
	}
}

function responseEnregistrement(rep){
	if(rep.error == undefined){
		makeConnexionPanel();
	}else{
		func_erreur("erreur");
	}
}

function deconnection(){
	if(connection){
		deconnect(env.key);
	}
}

function deconnect(key){
	$.ajax({
		type: "GET",
        url: "http://localhost:8080/Projet_3i017_AA/user/Logout",
        data: "key=" + key,
        dataType: "JSON",
        success: function(res) {
            reponseDeconnection(res);
        },
        error: function(tqXHR, textStatus, errorT) {
            func_erreur(textStatus);
        }
	});
}

function reponseDeconnection(rep){
	connection = false;
    if (rep.error == undefined) {
        makeConnexionPanel();
    } else {
        func_erreur(rep.error);
	}
}