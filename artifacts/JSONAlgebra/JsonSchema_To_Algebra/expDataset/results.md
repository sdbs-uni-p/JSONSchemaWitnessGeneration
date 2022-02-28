Validation test suite : 

	- Total number of schemas : 849	     Average size : 83.47 bytes       (most of them are duplicates, nb of schemas when eliminating the duplicates : 296)
	
	- ourTool : 
		- Correct : 849 out of 849 : 100%
		- Total time : 2579ms
		- Average time : 3.04ms
		
	- jsongenerator : 
		- Errors : 111 out of 849 : ~ 13.07%
		- Passed : 738 out of 849 : ~ 86.93%
		- Incorrect : 33 out of 849 : ~ 3.89%. 33 out of 738 : ~ 4.47%
		- Correct : 705 out of 849 : ~ 83.04. 705 out of 738 : ~ 95.53%
		- Total time : 53633ms
		- Average time : 72.67ms

	- N.B. seems that many schemas are the same (we can find the same schema in draft4, draft6 ..., at least this what I noticed in the ones that don't work).
	  Maybe we should probably focus only on schemas of draft2019 (contains more schemas. Check if schemas of draft4 sub schemas of draft6 sub schemas of draft7 sub schemas of draft2019)  

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


Containment draft4 VS IBM subschema VS jsongenerator: 

	- Total number of schemas : 1331
	
	- ourTool :
		- Correct : 1331 out of 1331 : 100%
		
	- subschema :
		- Errors : 838 out of 1331 : 62.96%
		- Passed : 493 out of 1331 : 37.04%
		- Incorrect : 15 out of 1331 : 1.13%. 15 out of 493 : 3.04%
		- Correct : 478 out of 1331 : 35.91%. 478 out of 493 : 96.96%
		
	- jsongenerator :
		- Errors : 594 out of 1331 44.63%
		- Passed : 737 out of 1331 : 55.37%
		- Incorrect : 340 out of 1331 : 25.54%. 340 out of 737 : 46.13%
		- Correct : 397 out of 1331 : 29.83%. 397 out of 737 : 53.87%
	
	
		- sat : 450 schemas     Average size : 382.04 bytes
			
			- ourTool :
				- Correct : 450 our of 450 : 100%
				- Total time : 2225ms
				- Average time : 4.94ms
				
			- subschema : 
				- Errors : 270 out of 450 : 60% (UnsupportedEnumCanonicalization, SchemaError, UnsupportedNegatedObject, AttributeError, JsonRefError, UnsupportedNegatedArray)
				- Passed : 180 out of 450 : 40%
				- Incorrect : 2 out of 450 : 0.44%. 2 out of 180 : 1.11%
				- Correct : 178 out of 450 : 39.56%. 178 out of 180 : 98.89%
				- Total time : 4449.05ms
				- Average time : 10.11ms
				
			- jsongenerator : 
				- Errors : 49 out of 450 : 10.89%
				- Passed : 401 out of 450 : 89.11%
				- Incorrect : 4 out of 450 : 0.89%. 4 out of 401 : 1%
				- Correct : 397 out of 450 : 88.22%. 397 out of 401 : 99%
				- Total time : 24315ms
				- Average time : 60.64ms
			
			
		- unsat : 881 schemas     Average size : 589.66 bytes
		
			- ourTool :
				- Correct : 881 out of 881 : 100%
				- Total time : 3751ms
				- Average time : 4.26ms
				
			- subschema : 
				- Errors : 568 out of 881 : 64.47%
				- Passed : 313 out of 881 : 35.53%
				- Incorrect : 13 out of 881 : 1.48%. 13 out of 313 : 4.15%
				- Correct : 300 out of 881 : 34.05%. 300 out of 313 : 95.85%
				- Total time : 13657.38ms
				- Average time : 16.11ms
				
			- jsongenerator : 
				- Errors : 545 out of 881 : 61.86%
				- Passed : 336 out of 881 : 38.14%
				- Incorrect : 336 out of 881 : 38.14%. 336 out of 336 : 100%
				- Correct : 0 out of 881 : 0%. 0 out of 336 : 0%
				- Total time : 32406ms
				- Average time : 59.35ms
				
        	
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


Containment full dataset VS jsongenerator :

	- Total number of schemas : 1713
	
	
	- ourTool :
		- Correct : 1713 out of 1713 : 100%
		
	- jsongenerator :
		- Errors : 501 out of 1713 : 29.25%
		- Passed : 1212 out of 1713 : 70.75%
		- Incorrect : 707 out of 1713 : 41.27%. 707 out of 1212 : 58.33%
		- Correct : 505 out of 1713 : 29.48%. 505 out of 1212 : 41.67%
		
		
		- sat : 591 schemas     Average size : 84.87 bytes  
			
			- ourTool : 
				- Correct : 591 out of 591 : 100% 
				- Total time : 3431ms
				- Average time : 5.81ms
				
			- jsongenerator : 
				- Errors : 58 out of 591 : ~ 9.81%
				- Passed : 533 our of 591 : ~ 90.19%
				- Incorrect : 28 out of 591 : ~ 4.74%. 28 out of 533 : ~ 5.25% 
				- Correct : 505 out of 591 : ~ 85.45%. 505 our of 533 : ~ 94.75%
				- Total time : 28452ms
				- Average time : 53.38ms
				
		- unsat : 1122 schemas    Average size : 123.26 bytes 
		
			- ourTool : 
				- Correct : 1122 out of 1122 : 100%
				- Total time : 4489ms
				- Average time : 4ms
				
			- jsongenerator :
				- Errors : 443 out of 1122 : ~ 39.48%
				- Passed : 679 out of 1122 : ~ 60.52%
				- Incorrect : 679 out of 1122 : ~ 60.47%. 679 out of 679 : 100%
				- Correct : 0 out of 1122 : 0%. 0 out of 679 : 0%
				- Total time : 35845ms
				- Average time : 52.79ms
				
				

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



Witness generation VS jsongenerator : handwritten testtricky         Average size : 585.52 bytes

	- Total number of schemas : 93 (originally 94, but for 1 schema we failed and answered no witness)
	
		- ourTool : 
			- Correct : 93 out of 93 : 100%
			- Total time : 248350ms
			- Average time : 2670.43ms
		
		- jsongenerator : 
			- Errors : 48 out of 93 : ~ 51.61% (1 of them is an infinite loop)
			- Passed : 45 out of 93 : ~ 48.39%
			- Incorrect : 36 out of 93 : ~ 38.71%. 36 out of 45 : ~ 80%
			- Correct : 9 out of 93 : ~ 9.68%. 9 out of 44 : ~ 20%
			- Total time : 7252ms
			- Average time : 161.16ms
			
			
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


Witness generation VS jsongenerator : realWorldSchemas		 
		
	- Total number of schemas : 6427
	
	- ourTool : 
		- Errors : 43 out of 6427 : 0.67%
		- Passed : 6384 out of 6427 : 99.33%
		- Incorrect : 16 out of 6427 : 0.25%. 16 out of 6384 : 0.25%
		- Correct : 6368 out of 6427 : 99.08%. 6368 out of 6384 : 99.75%
		
	- jsongenerator :
		- Errors : 314 out of 6427 : 4.89%
		- Passed : 6113 out of 6427 : 95.11%
		- Incorrect : 107 out of 6427 : 1.66%. 107 out of 6113 : 1.75%
		- Correct : 6006 out of 6427 : 93.45%. 6006 out of 6113 : 98.25%
		
		
	
		- sat : 6387 schemas     Average size : 8571.37 bytes
	
			- ourTool : 
				- Errors : 43 out of 6387 : 0.67%			
				- Passed : 6344 out of 6387 : ~ 99.33%
				- Incorrect : 16 out of 6387 : ~ 0.25%. 16 out of 6344 : ~ 0.25% (because of interpretation of oneOf as anyOf, with anyOf it takes a very long time)
				- Correct : 6328 out of 6387 : ~ 99.08%. 6328 out of 6344 : ~ 99.75%
				- Total time : 16702967ms
				- Average time : 2632.88ms
				
			- jsongenerator :
				- Errors : 303 out of 6387 : ~ 4.74%
				- Passed : 6084 out of 6387 : ~ 95.26%
				- Incorrect : 78 out of 6387 : ~ 1.22%. 78 out of 6084 : ~ 1.28%
				- Correct : 6006 out of 6387 : ~ 94.03%. 6006 out of 6084 : ~ 98.72%
				- Total time : 633799ms 
				- Average time : 104.17ms
				
						
			- Our 44 errors in jsongenerator results : 
				- 7 errors  
				- 3 false
				- 34 true
				
			- Our 27 incorrect witnesses in jsongenerator results :
				- 3 errors
				- 12 false
				- 12 true
				
				
		
		- unsat : 40 schemas     Average size : 3272.05 bytes
		
			- ourTool : 
				- Correct : 40 out of 40 : 100%
				- Total time : 2825ms
				- Average time : 70.63ms
			
			- jsongenerator :
				- Errors : 11 out of 40 : 27.5% (2 infinite loops)
				- Passed : 29 out of 40 : 72.5%
				- Incorrect : 29 out of 40 : 72.5%. 29 out of 29 : 100%
				- Correct : 0 out of 40 : 0%. 0 out of 29 : 0%
				- Total time : 8974ms
				- Average time : 309.45ms
			
			
			
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
Max file sizes 

	- realWorldSchemas:
		- sat : 1172935 bytes
		- unsat : 22645 bytes
	- handwritten : 2121 bytes
	- containment draft4 :
		- sat : 1513 bytes (after modification of const to enum using python : schemas are not written in one line anymore, that's why the size increased)
		- unsat : 2989 bytes
	- containment full :
		- sat : 260 bytes
		- unsat : 872 bytes
		
		
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
Avg file sizes

	- handwritten : 585.516 bytes
	- containment draft4 : 
		- all files together : 519.466 bytes
		- sat : 382.044 bytes
		- unsat : 589.658 bytes
	- realWorldSchemas :
		- all : 8913.23 bytes
		- sat : 8948.56 bytes
		- unsat : 3272.05 bytes
		
		
		
