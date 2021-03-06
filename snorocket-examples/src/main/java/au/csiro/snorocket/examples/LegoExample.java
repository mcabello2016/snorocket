/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.snorocket.examples;

import java.util.HashSet;
import java.util.Set;

import au.csiro.ontology.Factory;
import au.csiro.ontology.Node;
import au.csiro.ontology.Ontology;
import au.csiro.ontology.classification.IReasoner;
import au.csiro.ontology.model.Axiom;
import au.csiro.ontology.model.Concept;
import au.csiro.ontology.model.Role;
import au.csiro.snorocket.core.SnorocketReasoner;

/**
 * @author Alejandro Metke
 *
 */
public class LegoExample {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Running Chest Pain Lego Example");
        
        // Create the reasoner instance - the parametrised type refers to the
        // type of the external identifiers. In this case String is used and the
        // concepts are identified by sctid (but UUIDs could also be used).
        IReasoner reasoner = new SnorocketReasoner();
        
        // This set will contain the axioms
        Set<Axiom> axioms = new HashSet<Axiom>();
        
        // The factory returns IConcepts - in this case the actual type is
        // INamedConcept<String>
        Concept nonCardiacChestPain = Factory.createNamedConcept("274668005");
        Concept duringExcersice = Factory.createNamedConcept("309604004");
        Concept interview = Factory.createNamedConcept("108217004");
        Concept present = Factory.createNamedConcept("52101004");
        
        // The factory can also be used to create roles
        Role associatedWith = Factory.createNamedRole("47429007");
        
        // We need to define exactly what the identifier for the new concept 
        // will be
        Concept historyCardioStandardNonAnginalChestPainExertion = Factory.createNamedConcept("pce_24220");
        
        // This is the axiom created for the discernable
        axioms.add(
            Factory.createConceptInclusion(
                historyCardioStandardNonAnginalChestPainExertion, 
                Factory.createConjunction(
                        nonCardiacChestPain,
                        Factory.createExistential(associatedWith, duringExcersice)
                )
        ));
        
        // This is the axiom created for the qualifier
        axioms.add(
            Factory.createConceptInclusion(
                historyCardioStandardNonAnginalChestPainExertion, 
                interview
            )
        );
        
        // This is the axiom created for the value
        axioms.add(
            Factory.createConceptInclusion(
                historyCardioStandardNonAnginalChestPainExertion, 
                present
            )
        );
        
        // The first time the classify method is called it runs a full 
        // classification. In this example there is no base state loaded so
        // a full (and not very useful) classification will be excuted.
        reasoner.loadAxioms(axioms);
        reasoner.classify();
        
        // The taxonomy contains the inferred hierarchy
        Ontology t = reasoner.getClassifiedOntology();
        
        // We can look for nodes using the concept ids.
        Node newNode = t.getNode("pce_24220");
        System.out.println("Node for HISTORY_CARDIO_Standard_Non_Anginal_" +
        		"Chest_Pain_Exertion:\n  "+
                newNode.getEquivalentConcepts());
        
        
        // We can now look for the parent and child nodes
        Set<Node> parentNodes = newNode.getParents();
        System.out.println("Parents:");
        for(Node parentNode : parentNodes) {
            System.out.println("  "+parentNode.getEquivalentConcepts());
        }
        
        Set<Node> childNodes = newNode.getChildren();
        System.out.println("Children:");
        for(Node childNode : childNodes) {
            System.out.println("  "+childNode.getEquivalentConcepts());
        }

    }

}
