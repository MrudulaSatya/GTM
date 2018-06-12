/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gtm;

/**
 *
 * @author MRUDULA
 */


import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.sql.ResultSet;

public class GTM {

    /**
     * @param args the command line arguments
     */
    
      
    public static void main(String[] args) {
        // TODO code application logic here
             
            //Database for reading the synonyms
            DBConn conn = new DBConn();
            conn.getConnection();
            conn.stmtConnection();
            
            //xml file reading for reference and candidate xml files
            readXML xml = new readXML();
            xml.readXMLfile();
            NodeList candList, refList;
            candList = xml.getCandList();
            refList = xml.getRefList();
            
            String[] splitC;
            String[] splitR1;
           
        
            String tempC ;
           
        
        
            int p1=0;
            int countspaceR =0, countspaceC=0;
            
            
            double GTM;
            double precision;
            double recall;
            double avgLengthOfReferences,noOfwordsIncandi;
            //int flagR3,flagR2;
            //int flagp2R3,flagp2R2;
            //int flagp3R3 ,flagp3R2;
            //int ifBreak = 0;
         
	
        // loop that reads the xml one by one  
	for (int tempcand = 0; tempcand < candList.getLength(); tempcand ++) { 

		Node candNode = candList.item(tempcand);
                Node refNode = refList.item(tempcand);

		if (candNode.getNodeType() == Node.ELEMENT_NODE && refNode.getNodeType() == Node.ELEMENT_NODE) {

			Element candElement = (Element) candNode;
                        Element refElement = (Element) refNode;

			String cand;
                        cand = candElement.getElementsByTagName("DATA").item(0).getTextContent();
                        
                        String R1; 
                        R1 = refElement.getElementsByTagName("Ref1").item(0).getTextContent();
                      
                         //Display values
                        System.out.println("cand: " + cand);
                        System.out.println("ref1: " + R1);
                       
                        // splitting the candidate and reference sentences into individual words
                        String temp = cand.replaceAll("[\\n]"," ");
                        splitC = temp.replaceAll("[.,!?:;'।]","").replaceAll("-"," ").split(" ");
                        
                        for(int vx=0;vx<splitC.length;vx++){
                             if("".equals(splitC[vx])){
                                countspaceC++;
                            }
                        }  
                        int lenCand = (splitC.length - countspaceC);
                        
                        temp = R1.replaceAll("\\n]"," ");
                        splitR1 = temp.replaceAll("[.,!?:;'।]","").replaceAll("-"," ").split(" ");
                         
                        for(int zx=0;zx<splitR1.length;zx++){
                            if("".equals(splitR1[zx])){
                                countspaceR++;
                            }
                         }
                        int lenRef = (splitR1.length - countspaceR) ;
                       
                        //calculating c for the current candidate sentence
                        noOfwordsIncandi = lenCand;
                        avgLengthOfReferences = lenRef ;

                        //calculating p1
                        
                        //loop that traverses through each word of the candidate 
                        //one by one that is 1 gram precision
                        for(int i=0;i<lenCand;i++){
                            
                             //ith word stored in tempC 
                             tempC = splitC[i];
                             
                             //loop traverses R1 word by word
                             for(int j=0;j<lenRef;j++){
                                 
                                 //indication used later to 
                                 //see whether the word needs to be checked in R2
                                 //flagR2=0;
                                  
                                    if(tempC.equalsIgnoreCase(splitR1[j])){
                                         p1++; //word found in R1
                                         System.out.println(""+splitR1[j]);
                                         splitR1[j]="";//empty the word in R1 to avoid matching it again
                                         //flagR2=1;//no need to check in R2 for this word
                                         break;//comes out of the j loop
                                    } 
                                 
                              } //for for R1 ends 
                        }
                           
                        //print the final calculated p1
                        System.out.println("No of words in candidate ="+ lenCand);
                        System.out.println("No of words in Refernce = "+lenRef);
                        System.out.print("\n No of matched words = " + p1  + "\n");          
  
                        precision = p1/(double)lenCand;
                        recall = p1/(double)lenRef;
                        System.out.println("precison = "+precision); 
                        System.out.println("recall = "+recall);
                        
                        GTM = (2*precision*recall)/(precision + recall);
                        System.out.println("GTM score:" + GTM );
                                   
        } //if loop checking node type ends
                
               //resetting all values for next set of reference and candidate 
               p1 = 0; GTM =0 ; precision = 0; recall=0; countspaceR=0;countspaceC=0; 
      }// for loop reading the xml file ends
        
  }//main ends
        
}//class ends
    
