package com.example.ctq_ascenseur.data

import com.example.ctq_ascenseur.data.model.ControlPoint

object ControlPointData {
    val initialPoints = listOf(
        ControlPoint(1, "GAINE", "1.1 Parois de protection", "Vérification de l'état des parois"),
        ControlPoint(2, "GAINE", "1.2 Panneaux, portes de visite", "Contrôle des accès de secours"),
        ControlPoint(3, "GAINE", "1.3 Garde-pieds, seuils", "Vérification de la présence et fixation"),
        ControlPoint(4, "GAINE", "1.4 Moyen d'accès à la cuvette", "Échelle ou escalier conforme"),
        ControlPoint(5, "GAINE", "1.5 Éclairage", "Fonctionnement et intensité"),
        
        ControlPoint(6, "CUVETTE", "2.1 État général", "Propreté et absence d'eau"),
        ControlPoint(7, "CUVETTE", "2.2 Dispositif d'arrêt", "Fonctionnement du bouton STOP"),
        ControlPoint(8, "CUVETTE", "2.3 Demande de secours", "Fonctionnement de l'alarme en cuvette"),
        
        ControlPoint(9, "PORTES PALIÈRES", "5.1 Serrures", "Déverrouillage et contrôle électrique"),
        ControlPoint(10, "PORTES PALIÈRES", "5.2 Condamnations électriques", "Efficacité de la coupure"),
        
        ControlPoint(11, "CABINE", "7.1 Éléments constitutifs", "État des parois et du sol"),
        ControlPoint(12, "CABINE", "7.5 Portes de cabine", "Protection au passage"),
        ControlPoint(13, "CABINE", "7.8 Éclairage normal", "Fonctionnement"),
        
        ControlPoint(14, "SÉCURITÉ", "11.1 Parachute cabine", "État et fonctionnement"),
        ControlPoint(15, "SÉCURITÉ", "11.3 Limiteur de vitesse", "Vérification du déclenchement"),
        
        ControlPoint(16, "MACHINE", "13.1 Mécanismes", "État de la traction et des freins"),
        ControlPoint(17, "MACHINE", "13.5 Précision d'arrêt", "Nivelage au palier"),
        
        ControlPoint(18, "ÉLECTRICITÉ", "14.1 Circuit de terre", "Interconnexion des masses")
    )
}
