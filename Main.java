
 //Name : Ali Al-Saed 
 //ID : 1210198
 //Section : 2

package os;



import java.util.*;
import java.util.ArrayList;
public class Main {
    static int time = 300;
    static boolean processorIn = false;

    public static void main(String[] args) {
        Scanner  scan = new Scanner(System.in);
        int choice;

        while (true){
            System.out.println("1-Preemptive Priority");
            System.out.println("2-Non-preemptive Priority ");
            System.out.println("3-Multilevel Feedback Queue  ");

            System.out.println("4-Exit ");
            System.out.print("Enter your choice : ");
            choice = scan.nextInt() ;
            ArrayList <Process>Process = new ArrayList<Process>();
            Process.add(new Process(1,0,15,5,3));
            Process.add(new Process(2,1,23,14,2));
            Process.add(new Process(3,3,14,6,3));
            Process.add(new Process(4,4,16,15,1));
            Process.add(new Process(5,6,10,13,0));
            Process.add(new Process(6,7,22,4,1));
            Process.add(new Process(7,8,28,10,2));

            switch (choice){

                case 1:
                    preemPriority(Process,5);
                    break;
                case 2:
                    NoPreemPriority(Process,2);
                    break;
                case 4:
                    return;
                case 3:
                    runMultiLevelFeedbackQueue(Process,8);
                    break;


            }

        }


    }






    static void preemPriority(ArrayList<Process> Processes, int quantum) {
        System.out.println("Preemptive Priority Algorithm  ");
        System.out.println("---------------------------------------------------------------------");
        ArrayList<Process> WaitingQueue = new ArrayList<Process>();
        ArrayList<Process> ReadyQueue = new ArrayList<Process>();
        Process executingProcess = null;
        int totalTurnAroundTime = 0;
        double avgWaitingTime;
        double avgTurnAroundTime;
        int totalWaitingTime = 0;
        int totalTime = 0;
        boolean processorIn = false;
        Process TProcess = null;


        for(; totalTime <= time ; totalTime++){

            for (int i = 0 ; i < Processes.size(); i++){
                if(Processes.get(i).arrivalTime == totalTime){
                    ReadyQueue.add(Processes.remove(i));
                }
            }
            for (int i = 0; i < WaitingQueue.size(); i++) {
                Process process = WaitingQueue.get(i);
                if (totalTime - process.completionTime >= process.ioBurstTime) {
                    WaitingQueue.get(i).totalComesBackAfter += WaitingQueue.get(i).ioBurstTime;
                    ReadyQueue.add(WaitingQueue.remove(i));
                }
            }

            if(!processorIn && !ReadyQueue.isEmpty()){
                executingProcess = ReadyQueue.remove(0);
                processorIn = true;
            } else {



                if(executingProcess.remainingQuantum ==0) {
                    for(int i = 0 ; i< ReadyQueue.size();i++) {

                        if( executingProcess.priority == ReadyQueue.get(i).priority) {

                            System.out.print("P"+executingProcess.pid+"-->"+totalTime+"--");
                            executingProcess.remainingQuantum = 2 ;
                            ReadyQueue.add(executingProcess);
                            executingProcess =  ReadyQueue.remove(i);
                            processorIn = true ;



                        }

                        break;

                    }
                }





                Collections.sort(ReadyQueue, new Comparator<Process>(){
                    public int compare(Process p1, Process p2){
                        return p1.ppriorty - p2.ppriorty;
                    }
                });


                if (processorIn && !ReadyQueue.isEmpty() && ReadyQueue.get(0).ppriorty < executingProcess.ppriorty) {
                    System.out.print("P" + executingProcess.pid + "-->" + totalTime + "--");
                    ReadyQueue.add(executingProcess);
                    executingProcess = ReadyQueue.remove(0);
                }
            }

            for (Process p : ReadyQueue) {
                p.aging++;
                if (p.aging == quantum){
                    p.aging = 0;
                    p.ppriorty--;
                }
            }


            if(processorIn ){

                executingProcess.totalBurstTime++;
                executingProcess.remainingBurstTime--;
                executingProcess.remainingQuantum--;


                if(executingProcess.remainingBurstTime == 0 ){
                    executingProcess.remainingBurstTime=executingProcess.burstTime;
                    executingProcess.ppriorty = executingProcess.priority;
                    if (totalTime == 300){executingProcess.completionTime = totalTime;}else {
                        executingProcess.completionTime = totalTime +1 ;}

                    WaitingQueue.add(executingProcess);
                    processorIn = false;

                    System.out.print("Finish{P" + executingProcess.pid + "}"  + "-->" + executingProcess.completionTime + "--");
                    ///////////////////////
                    TProcess = executingProcess;
                    ReadyQueue.remove(executingProcess);

                






                    if(executingProcess.porcesseIn)
                        for (Process pp : Processes){
                            if(pp.pid ==  executingProcess.pid){
                                pp.completionTime = executingProcess.completionTime;
                                pp.totalBurstTime = executingProcess.totalBurstTime;
                            }
                        }
                    if (!executingProcess.porcesseIn){
                        Processes.add(executingProcess);
                        executingProcess.porcesseIn =true;
                    }
                }


            }



        }
        executingProcess.completionTime = totalTime -1;
        System.out.print("End{P" + executingProcess.pid + "}"  + "-->" + executingProcess.completionTime + "--");
        System.out.println("\n");

        boolean infFlag = true ;
        int counter = 0 ;
        for (Process p : Processes) {
            counter ++ ;
            if(counter ==7 ) {
                infFlag = false;
                break;
            }

        }



        // Calculate and print turnaround time and waiting time for each process
        for (Process p : Processes) {
            p.turnAroundTime = p.completionTime - p.arrivalTime;
            p.waitingTime = p.turnAroundTime - p.totalBurstTime - p.totalComesBackAfter;
            if (p.waitingTime < 0) {
                p.waitingTime = 0;
            }

            System.out.println("\nP" + p.pid + "|"+  "\tProcess TAT :" + p.turnAroundTime  +"|"+ "\tProcess TWT:" + p.waitingTime+"|");
            executingProcess.processEnter = 1;
            totalWaitingTime += p.waitingTime;
            totalTurnAroundTime += p.turnAroundTime;
        }

        avgTurnAroundTime = (double) totalTurnAroundTime / Processes.size();
        avgWaitingTime = (double) totalWaitingTime / Processes.size();

        if(infFlag == false) {
            System.out.println("\nAverage Waiting Time: " + avgWaitingTime);
            System.out.println("Average Turnaround Time: " + avgTurnAroundTime);
            System.out.println("---------------------------------------------------------------------");
        }
        else if ((infFlag == true)) {
            System.out.println("\nAverage Waiting Time: INF " );
            System.out.println("Average Turnaround Time: INF " );
            System.out.println("---------------------------------------------------------------------");
        }

    }

    static void NoPreemPriority(ArrayList<Process> Processes, int quantum) {
        System.out.println(" Non Preemptive Priority Algorithm with " );
        System.out.println("---------------------------------------------------------------------");
        ArrayList<Process> WaitingQueue = new ArrayList<Process>();
        ArrayList<Process> ReadyQueue = new ArrayList<Process>();
        Process executingProcess = null;
        Process TProcess = null;

        int totalTurnAroundTime = 0;
        double avgWaitingTime;
        double avgTurnAroundTime;
        int totalWaitingTime = 0;
        int totalTime = 0;
        boolean processorIn = false;
        int remainingQuantum = 0;


        for(; totalTime <= time ; totalTime++){







            for (int i = 0 ; i < Processes.size(); i++){
                if(Processes.get(i).arrivalTime == totalTime){
                    ReadyQueue.add(Processes.remove(i));
                }
            }




            for (int i = 0; i < WaitingQueue.size(); i++) {
                Process process = WaitingQueue.get(i);
                if (totalTime - process.completionTime >= process.ioBurstTime) {
                    WaitingQueue.get(i).totalComesBackAfter += WaitingQueue.get(i).ioBurstTime;
                    ReadyQueue.add(WaitingQueue.remove(i));
                }
            }



            if(!processorIn && !ReadyQueue.isEmpty()){


                executingProcess = ReadyQueue.remove(0);
                processorIn = true;
                remainingQuantum = executingProcess.remainingBurstTime;













                for(int i = 0 ; i< ReadyQueue.size();i++) {
                    if( executingProcess.ppriorty == ReadyQueue.get(i).ppriorty ) {
                        executingProcess.completionTime = totalTime +executingProcess.remainingQuantum;


                        System.out.print("P"+executingProcess.pid+"-->"+(executingProcess.completionTime )+"--");
                        executingProcess.remainingQuantum = 2 ;
                        remainingQuantum = 2;
                        ReadyQueue.add(executingProcess);
                        executingProcess =  ReadyQueue.remove(i);

                        processorIn = true ;



                    }


                    break;

                }





            } else {


                if(remainingQuantum ==0) {

                    for(int i = 0 ; i< ReadyQueue.size();i++) {
                        if ( ReadyQueue.get(i).ppriorty < executingProcess.ppriorty) {






                            executingProcess.completionTime = totalTime +executingProcess.remainingQuantum;
                            System.out.print("P"+executingProcess.pid+"-->"+(executingProcess.completionTime )+"--");

                            ReadyQueue.add(executingProcess);


                            executingProcess = ReadyQueue.remove(i);

                            remainingQuantum = executingProcess.remainingBurstTime;
                            executingProcess.remainingQuantum = executingProcess.remainingBurstTime ;
                            processorIn = true ;



                        }
                    }



                    for(int i = 0 ; i< ReadyQueue.size();i++) {
                        if( executingProcess.ppriorty == ReadyQueue.get(i).ppriorty) {






                            executingProcess.completionTime = totalTime +executingProcess.remainingQuantum;


                            System.out.print("P"+executingProcess.pid+"-->"+(executingProcess.completionTime )+"--");

                            executingProcess.remainingQuantum = 2 ;
                            remainingQuantum = 2;
                            ReadyQueue.add(executingProcess);

                            executingProcess =  ReadyQueue.remove(i);

                            processorIn = true ;



                        }


                        break;

                    }
                }


                Collections.sort(ReadyQueue, new Comparator<Process>(){
                    public int compare(Process p1, Process p2){
                        return p1.ppriorty - p2.ppriorty;
                    }
                });



            }



            if(processorIn ){

                executingProcess.totalBurstTime++;
                executingProcess.remainingBurstTime--;
                remainingQuantum--;
                executingProcess.processEnter = 1;



                if(executingProcess.remainingBurstTime == 0  ){



                    executingProcess.remainingBurstTime=executingProcess.burstTime;
                    executingProcess.ppriorty = executingProcess.priority;
                    if (totalTime == 300){executingProcess.completionTime = totalTime;}
                    else {
                        executingProcess.completionTime = totalTime +1;

                    }



                    WaitingQueue.add(executingProcess);
                    processorIn = false;


                    System.out.print("Finish{P" + executingProcess.pid + "}"  + "-->" + executingProcess.completionTime + "--");
                    TProcess = executingProcess;
                    ReadyQueue.remove(executingProcess);




                    for(int i = 0 ; i< ReadyQueue.size();i++) {
                        if( TProcess.priority == ReadyQueue.get(i).priority) {
                            executingProcess =  ReadyQueue.remove(i);
                            processorIn = true ;

                        }

                        break;

                    }


                    if(executingProcess.porcesseIn)
                        for (Process pp : Processes){
                            if(pp.pid ==  executingProcess.pid){
                                pp.completionTime = executingProcess.completionTime;
                                pp.totalBurstTime = executingProcess.totalBurstTime;
                            }
                        }
                    if (!executingProcess.porcesseIn){
                        Processes.add(executingProcess);
                        executingProcess.porcesseIn =true;
                    }
                }










            }


        }
        executingProcess.completionTime = totalTime-1;
        System.out.print("End{P" + executingProcess.pid + "}"  + "-->" + executingProcess.completionTime + "--");
        System.out.println("\n");

        boolean infFlag = true ;
        int counter = 0 ;
        for (Process p : Processes) {
            counter++;

            if(counter ==7 )
                infFlag = false;

        }


        for (Process p : Processes) {



            p.turnAroundTime = p.completionTime - p.arrivalTime;

            p.waitingTime= p.turnAroundTime - p.totalBurstTime  - p.totalComesBackAfter;

            if(p.waitingTime < 0) {
                p.waitingTime = 0;
            }
            System.out.println("\nP" + p.pid + "|"+  "\tProcess TAT :" + p.turnAroundTime  +"|"+ "\tProcess TWT:" + p.waitingTime+"|");


            totalWaitingTime += p.waitingTime;
            totalTurnAroundTime += p.turnAroundTime;

        }



        avgTurnAroundTime = (double) totalTurnAroundTime / Processes.size();
        avgWaitingTime = (double) totalWaitingTime / Processes.size();

        if(infFlag == false) {
            System.out.println("\nAverage Waiting Time: " + avgWaitingTime);
            System.out.println("Average Turnaround Time: " + avgTurnAroundTime);
            System.out.println("---------------------------------------------------------------------");
        }
        else if ((infFlag == true)) {
            System.out.println("\nAverage Waiting Time: INF " );
            System.out.println("Average Turnaround Time: INF " );
            System.out.println("---------------------------------------------------------------------");
        }

    }











    static void runMultiLevelFeedbackQueue(ArrayList<Process> processes, int quantum) {
        System.out.println("Multilevel Feedback Queue ");
        System.out.println("---------------------------------------------------------------------");

        ArrayList<Process> waitingQueue = new ArrayList<>();
        ArrayList<Process> readyQueue = new ArrayList<>();
        Process executingProcess = null;
        int totalTurnAroundTime = 0;
        double avgWaitingTime;
        double avgTurnAroundTime;
        int totalWaitingTime = 0;
        int totalTime = 0;
        int remainingQuantum = quantum;



        for (; totalTime <= time; totalTime++) {

            // Check for arriving processes at the current time
            for (int i = 0; i < processes.size(); i++) {
                if (processes.get(i).arrivalTime == totalTime) {
                    readyQueue.add(processes.remove(i));
                    i--; // Adjust index after removal
                }
            }

            // Check for processes returning from I/O burst
            for (int i = 0; i < waitingQueue.size(); i++) {
                Process process = waitingQueue.get(i);
                if (totalTime - process.completionTime >= process.ioBurstTime) {
                    waitingQueue.get(i).totalComesBackAfter += waitingQueue.get(i).ioBurstTime;
                    readyQueue.add(waitingQueue.remove(i));
                    i--; // Adjust index after removal
                }
            }

            // If the processor is free and there are processes in the ready queue, start a new process
            if (!processorIn && !readyQueue.isEmpty()) {
                executingProcess = readyQueue.remove(0);
                processorIn = true;
                remainingQuantum = executingProcess.currentRemainingQuantum; // Reset quantum for the new process
            }



            // If a process is being executed
            if (processorIn && executingProcess != null) {
                executingProcess.remainingBurstTime--;
                executingProcess.totalBurstTime++;
                remainingQuantum--;
                if (executingProcess.remainingBurstTime <0)
                    executingProcess.remainingBurstTime =0;


                // Process has completed its burst time
                if (executingProcess.remainingBurstTime == 0 ) {
                    executingProcess.remainingBurstTime=executingProcess.burstTime;
                    executingProcess.currentRemainingQuantum=8;


                    executingProcess.completionTime = totalTime + 1;
                    waitingQueue.add(executingProcess);
                    processorIn = false;

                    System.out.print("Finish{P" + executingProcess.pid + "}"  + "-->" + executingProcess.completionTime + "--");


                    if (!executingProcess.porcesseIn) {
                        executingProcess.porcesseIn = true;
                        // executingProcess.remainingQuantum = 8 ;
                        processes.add(executingProcess);
                    }
                }

                // Process has finished its quantum but not its burst time
                else if (remainingQuantum == 0) {
                    if (executingProcess.remainingBurstTime > 0) {



                        if(executingProcess.currentRemainingQuantum==8) {
                            remainingQuantum=16;
                            executingProcess.currentRemainingQuantum=16;


                        }
                        else if(executingProcess.currentRemainingQuantum==16) {

                            remainingQuantum=executingProcess.remainingBurstTime;

                        }


                        waitingQueue.add(executingProcess);
                        executingProcess.completionTime = totalTime + 1;

                        processorIn = false;

                        System.out.print("P" + executingProcess.pid + "-->" + executingProcess.completionTime + "-->");
                    }
                }
            }
        }
        // Print the final process state if there's an executing process
        if (executingProcess != null && processorIn) {
            executingProcess.completionTime = totalTime - 1;
            executingProcess.totalBurstTime--;
            System.out.print("End{P" + executingProcess.pid + "}"  + "-->" + executingProcess.completionTime + "--");
        }

        System.out.println("\n");


        boolean infFlag = true ;
        int counter = 0 ;
        for (Process p : processes) {
            counter ++ ;
            if(counter ==7 ) {
                infFlag = false;
                break;
            }

        }



        // Calculate and print turnaround time and waiting time for each process
        for (Process p : processes) {
            p.turnAroundTime = p.completionTime - p.arrivalTime;
            p.waitingTime = p.turnAroundTime - p.totalBurstTime - p.totalComesBackAfter;
            if (p.waitingTime < 0) {
                p.waitingTime = 0;
            }

            System.out.println("\nP" + p.pid + "|"+  "\tProcess TAT :" + p.turnAroundTime  +"|"+ "\tProcess TWT:" + p.waitingTime+"|");
            executingProcess.processEnter = 1;
            totalWaitingTime += p.waitingTime;
            totalTurnAroundTime += p.turnAroundTime;
        }

        avgTurnAroundTime = (double) totalTurnAroundTime / processes.size();
        avgWaitingTime = (double) totalWaitingTime / processes.size();

        if(infFlag == false) {
            System.out.println("\nAverage Waiting Time: " + avgWaitingTime);
            System.out.println("Average Turnaround Time: " + avgTurnAroundTime);
            System.out.println("---------------------------------------------------------------------");
        }
        else if ((infFlag == true)) {
            System.out.println("\nAverage Waiting Time: INF " );
            System.out.println("Average Turnaround Time: INF " );
            System.out.println("---------------------------------------------------------------------");
        }
    }




}
