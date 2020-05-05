# InfluenceMaximization
 SJSU : CS-255 Project on "Deterministice Threshold based Influence Maximization Algorithm"
 
 # Team Members:
   1.Sriarm Priyatham Siram <br />
   2.Charulata Lodha

 # Project Description:
 Implementing the research paper for Twitter data set to find most influentential handles.
 
 Link to Paper : https://pdfs.semanticscholar.org/31ad/0c126c2cd5fb610f254fb32ace90ae2b6673.pdf
 
 Data Set :
 Twitter : https://snap.stanford.edu/data/ego-Twitter.html
 Facebook : http://socialnetworks.mpi-sws.org/data-wosn2009.html
 
 
 # Implementation Details:
   We have divided the implementation into two primary steps
   1. **Preprocessing**: We have defined an object hierachy to represent a directed weighted graph in code. A graph from the dataset is loaded and converted into the Graph object composed on Node. A Node object maintains the infomation of a vertex in the graph including its incident and outwards edges. Once the Graph object is created we serialize it to a file.
   
   2. **Algorithm**: We have created an efficienet implementation of Threshold Difference Greedy Algorithm, mentioned in the above paper. 
   
   *Highlists* : 
   #1.Use of Priority Queue
   We have reduced the time complexity for selecting the new seed with maximum influence at each step by using Priority Queue. So, this resulted in improvement of Time Complexity from O(n) to O(log(n)) for that step.
   #2. Serialization
   #3. Incremental model for Experiments to calculate spread size.
