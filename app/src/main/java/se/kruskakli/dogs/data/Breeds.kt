package se.kruskakli.dogs.data

import kotlinx.serialization.Serializable

/*
[                                                                                                                                                                                                          
    {"breeds":                                                                                                                                                                                             
     [                                                                                                                                                                                                     
         {"weight":                                                                                                                                                                                        
          {"imperial":"17 - 23",                                                                                                                                                                           
           "metric":"8 - 10"                                                                                                                                                                               
          },                                                                                                                                                                                               
          "height":                                                                                                                                                                                        
          {"imperial":"13.5 - 16.5",                                                                                                                                                                       
           "metric":"34 - 42"                                                                                                                                                                              
          },                                                                                                                                                                                               
          "id":222,                                                                                                                                                                                        
          "name":"Shiba Inu",                                                                                                                                                                              
          "bred_for":"Hunting in the mountains of Japan, Alert Watchdog",                                                                                                                                  
          "breed_group":"Non-Sporting",                                                                                                                                                                    
          "life_span":"12 - 16 years",                                                                                                                                                                     
          "temperament":"Charming, Fearless, Keen, Alert, Confident, Faithful",                                                                                                                            
          "reference_image_id":"Zn3IjPX3f"                                                                                                                                                                 
         }                                                                                                                                                                                                 
     ],                                                                                                                                                                                                    
     "id":"Z6h74tJ-D",                                                                                                                                                                                     
     "url":"https://cdn2.thedogapi.com/images/Z6h74tJ-D.jpg",                                                                                                                                              
     "width":1080,                                                                                                                                                                                         
     "height":1080                                                                                                                                                                                         
    }                                                                                                                                                                                                      
] 
*/


@Serializable
class Breeds : ArrayList<BreedsItem>()