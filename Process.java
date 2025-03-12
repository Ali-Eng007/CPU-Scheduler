
 //Name : Ali Al-Saed 
 //ID : 1210198
 //Section : 2

package os;

import java.util.*;
import java.util.ArrayList;
class Process{
    int pid;
    int waitingTime;
    int arrivalTime;
    int burstTime;
    int turnAroundTime;
    int completionTime = 0;
    int priority;
    int ioBurstTime;
    int remainingBurstTime;
    int totalBurstTime =0;
    int totalComesBackAfter =0;
    boolean porcesseIn = false;
    int remainingQuantum = 2;
    int currentRemainingQuantum =8;
    int aging = 0;
    int processEnter = 0 ;

    int ppriorty;

    Process(int pid, int arrivalTime, int burstTime, int ioBurstTime, int priority){
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.ioBurstTime = ioBurstTime;
        this.priority = priority;
        this.remainingBurstTime = burstTime;
        this.ppriorty = priority;
    }
}