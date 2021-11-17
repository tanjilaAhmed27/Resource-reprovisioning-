package com.example.UpgradeSpectrumUtilization;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.sun.tools.internal.ws.wsdl.document.jaxws.Exception;
import org.apache.commons.math3.analysis.function.Constant;
import org.apache.commons.math3.distribution.PoissonDistribution;


/**
 * Created by roiya on 6/26/20.
 */
public class SPECTRUMUTILIZATION {

    public static int free_slot;
    public static int capacity;
    public static Path old_path;
    public static int free_slot_2nd;
    public static Path old_path_2nd;
    public static boolean is2Path;
    public static int old_blocked = 0;
    public static int old_list_size = 0;
    public static int req_num = 0;
    public static int new_lightpath_C;
    public static int new_lightpath_L;
    public static List<Path> pathList_L = new ArrayList<Path>();
    public static List<Path> pathList_C = new ArrayList<Path>();
    public static List<Event> eventList = new ArrayList<Event>();
    public static Topology testTopo;
    public static int offered_request = 0;
    public static int global_time = 0;
    public static int blocked_100 = 0;
    public static Double path_osnr_db = 0.0;
    public static int mod_bpsk = 0;
    public static int mod_qpsk = 0;
    public static int mod_8qam = 0;
    public static int mod_16qam = 0;
    public static int mod_32qam = 0;
    public static int mod_64qam = 0;
    public static ArrayList<Double> cdf = new ArrayList<Double>();
    public static ArrayList<Double> util_link = new ArrayList<Double>();
    public static List<Link> links_over_80 = new ArrayList<>();
    public static List<Link> link_list = new ArrayList<>();
    public static List<Double> centrality_list = new ArrayList<>();
    public static List<Integer> first_block_list = new ArrayList<>();

    public static int modulation_dropped_rerouting = 0;
    public static int rer_modulation_dropped_5 = 0;
    public static int rer_modulation_dropped_4 = 0;
    public static int rer_modulation_dropped_3 = 0;
    public static int rer_modulation_dropped_2 = 0;
    public static int rer_modulation_dropped_1 = 0;

    public static int reprovisioning = 0;
    public static int modulation_dropped_reprovisioning = 0;
    public static int reprov_modulation_dropped_5 = 0;
    public static int reprov_modulation_dropped_4 = 0;
    public static int reprov_modulation_dropped_3 = 0;
    public static int reprov_modulation_dropped_2 = 0;
    public static int reprov_modulation_dropped_1 = 0;

    public static double l_modulation = 0.0;
    public static int request_inL = 0;
    public static double median = 0;
    public static int count_reroute = 0;




    public static void simulate_K_short() {
        while (true) {
            if (eventList.isEmpty())
                break;

            /*if(blocked_100 == 1){
                System.out.println(req_num);
                first_block_list.add(req_num);
                return;
            }*/
            Event ev = eventList.remove(0);
            req_num++;
            handleStart_K_Short(ev);

        }
    }

    public static void handleStart_K_Short(Event ev) {
        Path short_path = new Path();

        boolean no_path = false;
        if ((int) testTopo.pathMatrix[ev.getSource()][ev.getDest()].get(0) > 0) {
            short_path = (Path) testTopo.pathMatrix[ev.getSource()][ev.getDest()].get(1);
        } else
            no_path = true;

        if (no_path) {
            blocked_100++;
            return;
        }


        //int list_size = links_over_80.size();
        //find_links_over_80();
        //if (req_num==1299){
          //  System.out.println("Original C paths-----------------------------");
            //System.out.println(pathList_C.size());
           // for(int i=0;i<pathList_C.size();i++){
            //    Path p = pathList_C.get(i);
                //System.out.println(p.toString());
           // }
        //}

        //if (req_num==1301){
            /*System.out.println("new C paths--------");
            for(int i=0;i<20;i++){
                Path p = pathList_C.get(i);
                System.out.println(p.toString());
            }*/
        //    System.out.println("Moved to L paths------------------------------");
            //System.out.println(pathList_L.size());
        //    for(int i=0;i<pathList_L.size();i++){
         //       Path p = pathList_L.get(i);
               // System.out.println( p.toString());
         //   }
        //}

        if (req_num>=860) {
            pre_update(req_num);
        }

            free_slot = 0;
            capacity = 0;
            old_path = null;
            free_slot_2nd = 0;
            old_path_2nd = null;
            is2Path = false;

//            Path p = null;
//            for (int i = 0; i < Constants.maximum_path; i++) {
//                p = (Path) testTopo.pathMatrix[ev.getSource()][ev.getDest()].get(i + 1);
//                //Vector<Link> links1 = p.getLinks();
//                Boolean isExisting = check_existing_lightpath_C(p);
//                if (isExisting && is2Path == false) {
//                    subtract_bandwidth_availibility_C(old_path, 0, isExisting);
//                    return;
//                } else if (isExisting && is2Path == true) {
//                    subtract_bandwidth_availibility_C(old_path, 2, isExisting);
//                    return;
//                } else {
//                /* For new lightpath */
//                    if (check_bandwidth_availibility_C(p)) {
//                /* Allocate the available capacity along the path */
//                        subtract_bandwidth_availibility_C(p, 1, false);
//                        return;
//                    }
//                }
//            }

            free_slot = 0;
            capacity = 0;
            old_path = null;
            free_slot_2nd = 0;
            old_path_2nd = null;
            is2Path = false;



            //**************************************** L Band ******************************************************


            for (int i = 0; i < Constants.maximum_path; i++) {
                 Path p = null;
                p = (Path) testTopo.pathMatrix[ev.getSource()][ev.getDest()].get(i + 1);




                Boolean path_covered = isPath_in_list80(p, links_over_80);
                if (path_covered) {
                    Boolean isExisting = check_existing_lightpath_L(p);
                    if (isExisting && is2Path == false) {
                        subtract_bandwidth_availibility_L(old_path, 0, isExisting);
                        return;
                    } else if (isExisting && is2Path == true) {
                        subtract_bandwidth_availibility_L(old_path, 2, isExisting);
                        return;
                    } else {
                        //For new lightpath
                        if (check_bandwidth_availibility_L(p)) {
                            // Allocate the available capacity along the path
                            subtract_bandwidth_availibility_L(p, 1, false);
                            return;
                        }
                    }
                }
            }




        free_slot = 0;
        capacity = 0;
        old_path = null;
        free_slot_2nd = 0;
        old_path_2nd = null;
        is2Path = false;


//****************************************   C band   ******************************************************

         Path p = null;
        for (int i = 0; i < Constants.maximum_path; i++) {
            p = (Path) testTopo.pathMatrix[ev.getSource()][ev.getDest()].get(i + 1);
            //Vector<Link> links1 = p.getLinks();
            Boolean isExisting = check_existing_lightpath_C(p);
            if (isExisting && is2Path == false) {
                subtract_bandwidth_availibility_C(old_path, 0, isExisting);
                return;
            } else if (isExisting && is2Path == true) {
                subtract_bandwidth_availibility_C(old_path, 2, isExisting);
                return;
            } else {
                /* For new lightpath */
                if (check_bandwidth_availibility_C(p)) {
                /* Allocate the available capacity along the path */
                    subtract_bandwidth_availibility_C(p, 1, false);
                    return;
                }
            }
        }

        free_slot = 0;
        capacity = 0;
        old_path = null;
        free_slot_2nd = 0;
        old_path_2nd = null;
        is2Path = false;


        blocked_100++;


        return;
    }



/* ########################################### MAIN ##################################################################*/

    public static void main(String args[]) {
        //ArrayList<Double> bandwidth_block_ratio = new ArrayList<Double>();
        ArrayList<Double> req_blocked_100 = new ArrayList<Double>();
        ArrayList<Double> bpsk = new ArrayList<Double>();
        ArrayList<Double> qpsk = new ArrayList<Double>();
        ArrayList<Double> qam8 = new ArrayList<Double>();
        ArrayList<Double> qam16 = new ArrayList<Double>();
        ArrayList<Double> qam32 = new ArrayList<Double>();
        ArrayList<Double> qam64 = new ArrayList<Double>();
        ArrayList<Double> requests_offered = new ArrayList<Double>();
        ArrayList<Double> lightpath_C = new ArrayList<Double>();
        ArrayList<Double> lightpath_L = new ArrayList<Double>();

        ArrayList<Double> C_size = new ArrayList<Double>();
        ArrayList<Double> L_size = new ArrayList<Double>();


        ArrayList<Double> requests_inL = new ArrayList<Double>();
        ArrayList<Double> requests_inC = new ArrayList<Double>();


        ArrayList<Double> reprovisioned = new ArrayList<Double>();
        ArrayList<Double> mod_drop_reprov = new ArrayList<Double>();
        ArrayList<Double> reprov_mod_drop_1 = new ArrayList<Double>();
        ArrayList<Double> reprov_mod_drop_2 = new ArrayList<Double>();
        ArrayList<Double> reprov_mod_drop_3 = new ArrayList<Double>();
        ArrayList<Double> reprov_mod_drop_4 = new ArrayList<Double>();
        ArrayList<Double> reprov_mod_drop_5 = new ArrayList<Double>();

        ArrayList<Double> mod_drop_rer = new ArrayList<Double>();
        ArrayList<Double> rer_mod_drop_1 = new ArrayList<Double>();
        ArrayList<Double> rer_mod_drop_2 = new ArrayList<Double>();
        ArrayList<Double> rer_mod_drop_3 = new ArrayList<Double>();
        ArrayList<Double> rer_mod_drop_4 = new ArrayList<Double>();
        ArrayList<Double> rer_mod_drop_5 = new ArrayList<Double>();



        util_link = new ArrayList<Double>();
        for (int x = 0; x < 35; x++)
            util_link.add(0.0);


        for (int m = 0; m < Constants.num_runs; m++) {
            createEventList();
            //readEvents(Constants.num_req);
            /*for (int x=0; x<eventList.size();x++) {
                System.out.println(eventList.get(x).toString());
            }*/
            try {
                testTopo = new Topology("topo_BT.txt");

            } catch (IOException e) {
                return;
            }

            if (m == 0) {
                for (int i = 0; i < Constants.num_nodes; i++) {
                    for (int j = 0; j < Constants.num_nodes; j++) {
                        Link l = testTopo.getLink(i, j);
                        Link l_oppo = testTopo.getLink(j, i);
                        if (l != null && l_oppo != null) {
                            if (!isLink_inList(l, link_list) && !isLink_inList(l_oppo, link_list)) {
                                link_list.add(l);
                            }
                        }
                    }
                }
            }




            //find_links_over_80();

            simulate_K_short();

            //System.out.println("C size final " + pathList_C.size());
            //System.out.println("L size final " + pathList_L.size());

            //System.out.println("request_inL " + request_inL);
            //System.out.println("request_in C " + (offered_request - request_inL - blocked_100));



            //System.out.println("Offered Requests: "+ offered_request);

            req_blocked_100.add((double) blocked_100);
            bpsk.add((double) mod_bpsk);
            qpsk.add((double) mod_qpsk);
            qam8.add((double) mod_8qam);
            qam16.add((double) mod_16qam);
            qam32.add((double) mod_32qam);
            qam64.add((double) mod_64qam);
            requests_offered.add((double) offered_request);
            lightpath_C.add((double) new_lightpath_C);
            lightpath_L.add((double) new_lightpath_L);

            C_size.add((double) pathList_C.size());
            L_size.add((double) pathList_L.size());


            mod_drop_rer.add((double) modulation_dropped_rerouting);
            rer_mod_drop_1.add((double) rer_modulation_dropped_1);
            rer_mod_drop_2.add((double) rer_modulation_dropped_2);
            rer_mod_drop_3.add((double) rer_modulation_dropped_3);
            rer_mod_drop_4.add((double) rer_modulation_dropped_4);
            rer_mod_drop_5.add((double) rer_modulation_dropped_5);

            reprovisioned.add((double) reprovisioning);
            mod_drop_reprov.add((double) modulation_dropped_reprovisioning);
            reprov_mod_drop_1.add((double) reprov_modulation_dropped_1);
            reprov_mod_drop_2.add((double) reprov_modulation_dropped_2);
            reprov_mod_drop_3.add((double) reprov_modulation_dropped_3);
            reprov_mod_drop_4.add((double) reprov_modulation_dropped_4);
            reprov_mod_drop_5.add((double) reprov_modulation_dropped_5);

            requests_inL.add((double) request_inL);
            requests_inC.add((double) offered_request - request_inL - blocked_100 );




            blocked_100 = 0;
            eventList = new ArrayList<Event>();
            testTopo = null;
            capacity = 0;
            pathList_L = new ArrayList<Path>();
            pathList_C = new ArrayList<Path>();
            links_over_80 = new ArrayList<>();
            new_lightpath_C = 0;
            new_lightpath_L = 0;
            path_osnr_db = 0.0;
            old_path = null;
            mod_bpsk = 0;
            mod_qpsk = 0;
            mod_8qam = 0;
            mod_16qam = 0;
            mod_32qam = 0;
            mod_64qam = 0;
            offered_request = 0;
            global_time = 0;
            cdf = new ArrayList<Double>();
            req_num = 0;
            old_blocked = 0;
            request_inL = 0;
            reprovisioning = 0;
            modulation_dropped_rerouting = 0;
            rer_modulation_dropped_1 = 0;
            rer_modulation_dropped_2 = 0;
            rer_modulation_dropped_3 = 0;
            rer_modulation_dropped_4 = 0;
            rer_modulation_dropped_5 = 0;
            modulation_dropped_reprovisioning = 0;
            reprov_modulation_dropped_1 = 0;
            reprov_modulation_dropped_2 = 0;
            reprov_modulation_dropped_3 = 0;
            reprov_modulation_dropped_4 = 0;
            reprov_modulation_dropped_5 = 0;
            count_reroute = 0;
            median = 0;


        }

        /*******************************************     Output   ***************************************************/
        Collections.sort(first_block_list);
        for(int f= 0; f < first_block_list.size(); f++) {
           // System.out.println(first_block_list.get(f));
        }

        double avg_req_blocked_100 = avg_cal(req_blocked_100);
        double avg_bpsk = avg_cal(bpsk);
        double avg_qpsk = avg_cal(qpsk);
        double avg_qam8 = avg_cal(qam8);
        double avg_qam16 = avg_cal(qam16);
        double avg_qam32 = avg_cal(qam32);
        double avg_qam64 = avg_cal(qam64);
        double avg_offered_request = avg_cal(requests_offered);
        double avg_lightpath_C = avg_cal(lightpath_C);
        double avg_lightpath_L = avg_cal(lightpath_L);

        double avg_requests_inL = avg_cal(requests_inL);
        double avg_requests_inC = avg_cal(requests_inC);

        double avg_C_size = avg_cal(C_size);
        double avg_L_size = avg_cal(L_size);


        double avg_reprovisioned = avg_cal(reprovisioned);
        double avg_mod_drop_reprov = avg_cal(mod_drop_reprov);
        double avg_reprov_mod_drop_1 = avg_cal(reprov_mod_drop_1);
        double avg_reprov_mod_drop_2 = avg_cal(reprov_mod_drop_2);
        double avg_reprov_mod_drop_3 = avg_cal(reprov_mod_drop_3);
        double avg_reprov_mod_drop_4 = avg_cal(reprov_mod_drop_4);
        double avg_reprov_mod_drop_5 = avg_cal(reprov_mod_drop_5);

        double avg_mod_drop_rer = avg_cal(mod_drop_rer);
        double avg_rer_mod_drop_1 = avg_cal(rer_mod_drop_1);
        double avg_rer_mod_drop_2 = avg_cal(rer_mod_drop_2);
        double avg_rer_mod_drop_3 = avg_cal(rer_mod_drop_3);
        double avg_rer_mod_drop_4 = avg_cal(rer_mod_drop_4);
        double avg_rer_mod_drop_5 = avg_cal(rer_mod_drop_5);



        System.out.println("100 Gbps request blocked:" + (int) avg_req_blocked_100);
        System.out.println("Offered Request: " + (int) avg_offered_request);
        //System.out.println("Lightpath provisioned in C: " + (int) avg_lightpath_C);
        //System.out.println("Lightpath provisioned in L: " + (int) avg_lightpath_L);

            /*for(int y=0;y<35;y++) {
               System.out.println((util_link.get(y)/Constants.num_runs));
            }*/

        System.out.println((int) avg_bpsk);
        System.out.println((int) avg_qpsk);
        System.out.println((int) avg_qam8);
        System.out.println((int) avg_qam16);
        System.out.println((int) avg_qam32);
        System.out.println((int) avg_qam64);

        System.out.println("avg requests in C: " + avg_requests_inC);
        System.out.println("avg requests in L: " + avg_requests_inL);

        System.out.println("avg C lightpath size: " + avg_C_size);
        System.out.println("avg L lightpath size: " + avg_L_size);


        System.out.println("avg modulation dropped rerouting: "+ avg_mod_drop_rer);
        System.out.println("avg modulation dropped 1: "+ avg_rer_mod_drop_1);
        System.out.println("avg modulation dropped 2: "+ avg_rer_mod_drop_2);
        System.out.println("avg modulation dropped 3: "+ avg_rer_mod_drop_3);
        System.out.println("avg modulation dropped 4: "+ avg_rer_mod_drop_4);
        System.out.println("avg modulation dropped 5: "+ avg_rer_mod_drop_5);

        System.out.println("avg no of reprovisioned lightpaths "+ avg_reprovisioned);
        System.out.println("avg modulation dropped reprovisioning: "+ avg_mod_drop_reprov);
        System.out.println("avg modulation dropped 1: "+ avg_reprov_mod_drop_1);
        System.out.println("avg modulation dropped 2: "+ avg_reprov_mod_drop_2);
        System.out.println("avg modulation dropped 3: "+ avg_reprov_mod_drop_3);
        System.out.println("avg modulation dropped 4: "+ avg_reprov_mod_drop_4);
        System.out.println("avg modulation dropped 5: "+ avg_reprov_mod_drop_5);

    }


    public static double get_centrality(Link l) {
        int index = link_list.indexOf(l);
        return centrality_list.get(index);
    }

    public static void readEvents(int num) {
        try {
            FileReader f_read = new FileReader(Constants.event_file);
            BufferedReader b_read = new BufferedReader(f_read);


            for (int i = 0; i < num; i++) {
                String line = b_read.readLine();
                String[] parts = line.split(",");
                Event ev = new Event(Integer.parseInt(parts[0]), 0, "start", 100.0, Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                eventList.add(ev);
            }
        } catch (IOException ex) {

        }

    }

    // ###################################### Create events of minutes and add to list ###################################
    public static void createEventList() {
        for (int i = 0; i < Constants.num_nodes; i++) {
            cdf.add(0.0);
        }
        cdf.set(0, Constants.pmf.get(0));

        for (int i = 1; i < Constants.pmf.size(); i++) {
            cdf.set(i, Constants.pmf.get(i) + cdf.get(i - 1));
        }

        //long seed_src = 524355043817264027L;
        //long seed_dst = 4052996208757539541L;

        for (int i = 0; i < Constants.num_req; i++) {
            int src = 0;
            int dst = 0;

            //Random randomSelector = new Random(System.currentTimeMillis());

            Random random = new Random();
            double x = random.nextDouble() % 1;
            //System.out.println("x: " +x);

            //System.out.println("CDF: " +cdf.get(0));
            if (x < cdf.get(0))
                src = 0;
            for (int j = 0; j < cdf.size() - 1; j++) {
                if (x > cdf.get(j) & x < cdf.get(j + 1)) {
                    src = j + 1;
                    //System.out.println("src: " + src);
                }
            }

            double y = random.nextDouble() % 1;
            //System.out.println("y: " +y);
            if (y < cdf.get(0))
                dst = 0;
            for (int j = 0; j < cdf.size() - 1; j++) {
                if (y > cdf.get(j) & y < cdf.get(j + 1)) {
                    dst = j + 1;
                    //System.out.println("dst: " + dst);
                }
            }

            PoissonDistribution poisson = new PoissonDistribution(20);
            int start = poisson.sample();
            Double req_gbps = 100.0;

            if (src != dst) {
                int id = offered_request;
                offered_request++;
                Event ev = new Event(id, global_time + start, "start", req_gbps, src, dst);
                insertEventInSortedList(ev, global_time + start);
                //System.out.println(ev);
                //System.out.println("Source  " + src + " Destination   "+ dst);
            }
        }
    }

    public static void insertEventInSortedList(Event event, int _time) {
        if (eventList.isEmpty()) {
            eventList.add(event);
            return;
        }
        Event evnt;
        int pos;
        int list_event_time = 0;
        for (pos = 0; pos < eventList.size(); pos++) {
            evnt = eventList.get(pos);
            if (evnt.getEventType() == "start")
                list_event_time = evnt.getEventTimeStart();
            if (list_event_time >= _time)
                break;
        }
        if (pos >= eventList.size()) {
            eventList.add(event);
        } else {
            eventList.add(pos, event);
        }
    }

    /***************************************** high util 3 upgrades *********************************************/
    public static void pre_update(int req_num) {
        int[] numArray = new int[pathList_C.size()];
        for(int i=0; i<pathList_C.size(); i++) {
            Path p = pathList_C.get(i);

            Vector<Link> p_links = p.getLinks();
            int path_length = 0;
            for (int j = 0; j < p_links.size(); j++) {
                int src = p_links.get(j).startNodeID;
                int dst = p_links.get(j).endNodeID;
                Link l = testTopo.getLink(src, dst);
                path_length+=l.getLength();
            }
            numArray[i] = path_length;

        }
        Arrays.sort(numArray);

        if (numArray.length % 2 == 0)
            median = ((double)numArray[numArray.length/2] + (double)numArray[numArray.length/2 - 1])/2;
        else
            median = (double) numArray[numArray.length/2];



        if (req_num == (860)) {
            Link l = testTopo.getLink(5, 21);
            links_over_80.add(l);
            l = testTopo.getLink(5, 18);
            links_over_80.add(l);
            l = testTopo.getLink(11, 21);
            links_over_80.add(l);
            l = testTopo.getLink(10, 12);
            links_over_80.add(l);
            l = testTopo.getLink(6, 10);
            links_over_80.add(l);
            l = testTopo.getLink(10, 21);
            links_over_80.add(l);
            l = testTopo.getLink(4, 12);
            links_over_80.add(l);
            l = testTopo.getLink(2, 17);
            links_over_80.add(l);
            l = testTopo.getLink(16, 18);
            links_over_80.add(l);
            l = testTopo.getLink(2, 4);
            links_over_80.add(l);
            l = testTopo.getLink(6, 19);
            links_over_80.add(l);

            reprovisioning();
            reroutingCinCplusL();
            //System.out.println("rerouted 1st: " + count_reroute);
            //count_reroute = 0;
            System.out.println("re-provisioned: " + reprovisioning);
            reprovisioning = 0;
        }
        else if (req_num == (1048)){ //1163
            Link l = testTopo.getLink(11, 20);
                links_over_80.add(l);
                l = testTopo.getLink(5, 13);
                links_over_80.add(l);
                l = testTopo.getLink(0, 18);
                links_over_80.add(l);
                l = testTopo.getLink(8, 18);
                links_over_80.add(l);
                l = testTopo.getLink(7, 10);
                links_over_80.add(l);
                l = testTopo.getLink(0, 17);
                links_over_80.add(l);
                l = testTopo.getLink(6, 11);
                links_over_80.add(l);
                l = testTopo.getLink(1, 13);
                links_over_80.add(l);
                l = testTopo.getLink(4, 13);
                links_over_80.add(l);
                l = testTopo.getLink(9, 12);
                links_over_80.add(l);
                l = testTopo.getLink(3, 9);
                links_over_80.add(l);

            reprovisioning();
            reroutingCinCplusL();
            //System.out.println("rerouted 2nd: " + count_reroute);
            //count_reroute = 0;
            System.out.println("re-provisioned: " + reprovisioning);
            reprovisioning = 0;
        }
//
      else if (req_num == (1620)) { //2093
            Link l = testTopo.getLink(2, 3);
            links_over_80.add(l);
            l = testTopo.getLink(14, 16);
            links_over_80.add(l);
            l = testTopo.getLink(16, 17);
            links_over_80.add(l);
            l = testTopo.getLink(14, 17);
            links_over_80.add(l);
            l = testTopo.getLink(7, 9);
            links_over_80.add(l);
            l = testTopo.getLink(19, 20);
            links_over_80.add(l);
            l = testTopo.getLink(0, 8);
            links_over_80.add(l);
            l = testTopo.getLink(2, 15);
            links_over_80.add(l);
            l = testTopo.getLink(7, 20);
            links_over_80.add(l);
            l = testTopo.getLink(3, 15);
            links_over_80.add(l);
            l = testTopo.getLink(0, 1);
            links_over_80.add(l);
            l = testTopo.getLink(1, 8);
            links_over_80.add(l);
            l = testTopo.getLink(14, 15);
            links_over_80.add(l);

            reprovisioning();
            reroutingCinCplusL();
            //System.out.println("rerouted 3rd: " + count_reroute);
            //count_reroute = 0;

            System.out.println("re-provisioned: " + reprovisioning);
            reprovisioning = 0;
            System.out.println("blocking 3rd: " + blocked_100);

        }
    }

    public static void reroutingCinCplusL(){


        for(int i=0; i<pathList_C.size(); i++) {
            Path p = pathList_C.get(i);
            double ase = 0.0;

            Vector<Link> p_links = p.getLinks();
            for (int j = 0; j < p_links.size(); j++) {
                int src = p_links.get(j).startNodeID;
                int dst = p_links.get(j).endNodeID;
                Link l = testTopo.getLink(src, dst);
                if(isLink_inList(l,links_over_80)) {
                  //  System.out.println(l.toString());
                    double link_ase = read_xl(src + "_" + dst, dst + "_" + src, Constants.osnr_file_C);
                  //  System.out.println(link_ase + " " + l.toString());
                    ase += link_ase;
                }
                else {
                    double link_ase = read_xl(src + "_" + dst, dst + "_" + src, Constants.osnr_file_c);
                   // System.out.println(link_ase + " " + l.toString());
                    ase += link_ase;
                }

                //System.out.print("src: " + src + "dst: " + dst + "path_ase: " + path_ase);
            }
           // System.out.println(path_ase);
            double noise = ase + (p_links.size()+1) * Constants.node_ase;
            double osnr_mw = Constants.launch_power/noise;
            double osnr = 10*Math.log10(osnr_mw);

            double modulation_new = (double)osnr_thrsld_table(osnr);

            if (p.modulation != modulation_new){
                modulation_dropped_rerouting++;
                if((p.modulation - modulation_new)==1)
                    rer_modulation_dropped_1++;
                else if((p.modulation - modulation_new)==2)
                    rer_modulation_dropped_2++;
                else if((p.modulation - modulation_new)==3)
                    rer_modulation_dropped_3++;
                else if((p.modulation - modulation_new)==4)
                    rer_modulation_dropped_4++;
                else if((p.modulation - modulation_new)==5)
                    rer_modulation_dropped_5++;
                //add the request to event list

                releaseCapacity_C(p);
                pathList_C.remove(i);
                free_slot = 0;
                capacity = 0;
                old_path = null;
                free_slot_2nd = 0;
                old_path_2nd = null;
                is2Path = false;
                //System.out.println("old: " + p);
                //first try to use the same path
                Boolean isExisting = check_existing_lightpath_C(p);
                if (isExisting && is2Path == false) {
                    subtract_bandwidth_availibility_C(old_path, 0, isExisting);
                    //System.out.println("New: "+ p);
                    continue;
                } else if (isExisting && is2Path == true) {
                    subtract_bandwidth_availibility_C(old_path, 2, isExisting);
                    //System.out.println("New: "+ p);
                    continue;
                } else {
                /* For new lightpath */
                    if (check_bandwidth_availibility_C(p)) {
                /* Allocate the available capacity along the path */
                        subtract_bandwidth_availibility_C(p, 1, false);
                        //System.out.println("New: "+ p);
                        count_reroute++;
                        continue;
                    }

                }
                free_slot = 0;
                capacity = 0;
                old_path = null;
                free_slot_2nd = 0;
                old_path_2nd = null;
                is2Path = false;

//                int id = offered_request;
//                //offered_request++;
//                Event ev = new Event(id, 0, "start", 100.0, p.getSourceID(), p.getDestNodeID());
//                insertEventInSortedList(ev, 0);
//                count_reroute++;
                //remove current path

            }

           /* if(p.modulation < modulation_new) {
                double p_ase = 0.0;

                Vector<Link> new_links = p.getLinks();
                for (int k = 0; k < new_links.size(); k++) {
                    int src = new_links.get(k).startNodeID;
                    int dst = new_links.get(k).endNodeID;
                    Link l2 = testTopo.getLink(src, dst);
                    if(isLink_inList(l2,links_over_80)) {
                       // System.out.println(l.toString());
                        double link_ase = read_xl(src + "_" + dst, dst + "_" + src, Constants.osnr_file_C);
                        System.out.println(link_ase + " c-in-l " + l2.toString());
                        p_ase += link_ase;
                    }
                    else {
                        double link_ase = read_xl(src + "_" + dst, dst + "_" + src, Constants.osnr_file_c);
                        System.out.println(link_ase + " c " + l2.toString());
                        p_ase += link_ase;
                    }

                    //System.out.print("src: " + src + "dst: " + dst + "path_ase: " + path_ase);
                }
                System.out.println(p_ase + " new ");


                double p_noise = p_ase + (new_links.size()+1) * Constants.node_ase;
                double p_osnr_mw = Constants.launch_power/p_noise;
                double p_osnr = 10*Math.log10(p_osnr_mw);

                System.out.println("noise " + p_noise + " osnr_mw " + p_osnr_mw + " osnr " + p_osnr);

                System.out.println("old osnr " + p.path_osnr_db + " new osnr " + p_osnr);
                System.out.println("old mod " + p.modulation + " new mod " + modulation_new);

                p_ase = 0.0;
                for (int j = 0; j < new_links.size(); j++) {
                    int src = new_links.get(j).startNodeID;
                    int dst = new_links.get(j).endNodeID;
                    Link l2 = testTopo.getLink(src, dst);
                    double link_ase = read_xl(src + "_" + dst, dst + "_" + src, Constants.osnr_file_c);
                        //System.out.println(link_ase + " c " + l2.toString());
                    p_ase += link_ase;


                    //System.out.print("src: " + src + "dst: " + dst + "path_ase: " + path_ase);
                }
                System.out.println(p_ase + " old " + p.toString());
                p_noise = p_ase + (new_links.size()+1) * Constants.node_ase;
                p_osnr_mw = Constants.launch_power/p_noise;
                p_osnr = 10*Math.log10(p_osnr_mw);

                System.out.println("old noise " + p_noise + " osnr_nw " + p_osnr_mw + " osnr " + p_osnr);
            }*/
        }
        //System.out.println(list_size + " old new "+ pathList_C.size());
    }

   // public static int

    public static void reprovisioning(){
        //System.out.println(req_num + " C size = " + pathList_C.size());
        //System.out.println(req_num + " median = " + median);
        for(int i=0;i<pathList_C.size();i++){
            Path p = pathList_C.get(i);
            Vector<Link> links = p.getLinks();
            int len = 0;
            for (int j=0; j< links.size(); j++){
                len+=links.get(j).getLength();

            }


            Boolean path_covered = isPath_in_list80(p, links_over_80);


            if (path_covered  ) {
                free_slot = 0;
                capacity = 0;
                old_path = null;
                free_slot_2nd = 0;
                old_path_2nd = null;
                is2Path = false;
                //System.out.println ("old path " + p);
                Boolean isExisting = check_existing_lightpath_L(p);
                if (isExisting && is2Path == false) {
                    subtract_bandwidth_availibility_L(old_path, 0, isExisting);
                    releaseCapacity_C(p);
                    pathList_C.remove(i);
                    reprovisioning++;
                    if(p.modulation > l_modulation)
                        modulation_dropped_reprovisioning++;
                    if((p.modulation-l_modulation)==1)
                        reprov_modulation_dropped_1++;
                    else if((p.modulation-l_modulation)==2)
                        reprov_modulation_dropped_2++;
                    else if((p.modulation-l_modulation)==3)
                        reprov_modulation_dropped_3++;
                    else if((p.modulation-l_modulation)==4)
                        reprov_modulation_dropped_4++;
                    else if((p.modulation-l_modulation)==5)
                        reprov_modulation_dropped_5++;

                    continue;
                } else if (isExisting && is2Path == true) {
                    subtract_bandwidth_availibility_L(old_path, 2, isExisting);
                    releaseCapacity_C(p);
                    pathList_C.remove(i);
                    reprovisioning++;
                    if(p.modulation > l_modulation)
                        modulation_dropped_reprovisioning++;
                    if((p.modulation-l_modulation)==1)
                        reprov_modulation_dropped_1++;
                    else if((p.modulation-l_modulation)==2)
                        reprov_modulation_dropped_2++;
                    else if((p.modulation-l_modulation)==3)
                        reprov_modulation_dropped_3++;
                    else if((p.modulation-l_modulation)==4)
                        reprov_modulation_dropped_4++;
                    else if((p.modulation-l_modulation)==5)
                        reprov_modulation_dropped_5++;

                    continue;
                } else {
                    //For new lightpath
                    if (check_bandwidth_availibility_L(p)) {
                        // Allocate the available capacity along the path
                        subtract_bandwidth_availibility_L(p, 1, false);
                        releaseCapacity_C(p);
                        pathList_C.remove(i);
                        reprovisioning++;
                        if(p.modulation > l_modulation)
                            modulation_dropped_reprovisioning++;
                        if((p.modulation-l_modulation)==1)
                            reprov_modulation_dropped_1++;
                        else if((p.modulation-l_modulation)==2)
                            reprov_modulation_dropped_2++;
                        else if((p.modulation-l_modulation)==3)
                            reprov_modulation_dropped_3++;
                        else if((p.modulation-l_modulation)==4)
                            reprov_modulation_dropped_4++;
                        else if((p.modulation-l_modulation)==5)
                            reprov_modulation_dropped_5++;

                        continue;
                    }
                }
            }

        }

        //System.out.println(req_num + " L size = " + pathList_L.size());
        //System.out.println("request_inL " + request_inL);
        //System.out.println("request_in C " + (req_num -  - blocked_100));
    }

    public static void releaseCapacity_C(Path p) {

        //Calculate Modulation and capacity from path OSNR
        int modulation = (int)p.modulation;

        Vector<Link> links = p.getLinks();
        //free_slot = p.slot_taken;
        osnr_thrsld_table(p.path_osnr_db);
        if (modulation == 0) {
            //2 new slots for BPSK
            for (int i = 0; i < links.size(); i++) {
                int src = links.get(i).startNodeID;
                int dst = links.get(i).endNodeID;
                Link l = (Link) testTopo.adjMatrix[src][dst].get(1);
                l.slot_occupation_c.set(p.slot_taken, 0);
                l.slot_occupation_c.set(p.slot_taken + 1, 0);
                l.free_capacity_c.set(p.slot_taken, (double)capacity);
                l.free_capacity_c.set(p.slot_taken + 1, (double)capacity);
            }
        } else {
            for (int i = 0; i < links.size(); i++) {
                int src = links.get(i).startNodeID;
                int dst = links.get(i).endNodeID;
                Link l = (Link) testTopo.adjMatrix[src][dst].get(1);

                l.free_capacity_c.set(p.slot_taken, (l.free_capacity_c.get(p.slot_taken) + 100.0));


                if(l.free_capacity_c.get(p.slot_taken) == capacity)
                    l.slot_occupation_c.set(p.slot_taken, 0);

                //if((capacity - 100.0)<0)
                //   System.out.println("new path " + modulation + " " + (capacity));
            }
        }

    }

/* ***************************************** Occupancy Check *****************************************/
        public static List<Link> find_links_over_80(){


            for(int i=0; i< Constants.num_nodes; i++){
                for(int j=0; j< Constants.num_nodes; j++) {
                    Link l =  testTopo.getLink(i, j);
                    Link l_oppo = testTopo.getLink(j, i);
                    //System.out.println(l.toString());
                    if (l != null) {
                        //System.out.println(l.toString());
                        double count = 0.0;
                        for (int k = 0; k < Constants.channels_perLink; k++) {
                            if (l.slot_occupation_c.get(k) == 1)
                                count++;
                        }
                        count = count / Constants.channels_perLink;
                        if (count >= 0.5) {
                            if(!links_over_80.contains(l) && !links_over_80.contains(l_oppo)) {
                                links_over_80.add(l);
                                //System.out.println(l.toString());
                            }
                        }
                    }
                }

            }
            return links_over_80;
        }

        public static boolean isPath_in_list80(Path p, List<Link> list){
            Vector<Link> links = p.getLinks();
            boolean isCovered = true;
            for(int j = 0; j<links.size(); j++) {
                int src = links.get(j).startNodeID;
                int dst = links.get(j).endNodeID;
                Link l = (Link) testTopo.adjMatrix[src][dst].get(1);
                if(!isLink_inList80(l, list)){
                    isCovered = false;
                    return isCovered;
                }
            }
            return isCovered;
        }

        public static boolean isLink_inList80(Link l, List<Link> list){
        boolean isThere = false;
        for (int i = 0; i<list.size(); i++){
            Link l2 = list.get(i);
            if(((l.getStartNodeID()==l2.getStartNodeID())&& (l.getEndNodeID()==l2.getEndNodeID())) ||
                    ((l.getStartNodeID()==l2.getEndNodeID()) && (l.getEndNodeID()==l2.getStartNodeID()))){
                isThere = true;
                return isThere;
            }
        }
        return isThere;

    }
    public static boolean isLink_inList(Link l, List<Link> list){
        boolean isThere = false;
        for (int i = 0; i<list.size(); i++){
            Link l2 = list.get(i);
            if(((l.getStartNodeID()==l2.getStartNodeID())&& (l.getEndNodeID()==l2.getEndNodeID())) ||
                    ((l.getStartNodeID()==l2.getEndNodeID()) && (l.getEndNodeID()==l2.getStartNodeID()))){
                isThere = true;
                return isThere;
            }
        }
        return isThere;

    }
        // ############################################   For L, Check Lightpath Capacity  ###########################################

        public static boolean check_existing_lightpath_L(Path p){
        /* Check source-dst */
            /****** case where there is already a lightpath ******/

            Path match_path;
            for(int i = 0; i < pathList_L.size();i++){
                Path path = pathList_L.get(i);
                if(path.getSourceID()==p.getSourceID() && path.getDestNodeID()==p.getDestNodeID()){
                    match_path = path;
                /* we have a matching path, now see if capacity is available */
                    Vector<Link> links = match_path.getLinks();
                    boolean all_links_has_capacity = true;
                    for(int j = 0; j<links.size(); j++) {
                        int src = links.get(j).startNodeID;
                        int dst = links.get(j).endNodeID;
                        Link l = (Link) testTopo.adjMatrix[src][dst].get(1);

                        if (l.free_capacity_l.get(match_path.slot_taken) < 100) {
                            all_links_has_capacity = false;
                        }
                    }
                    if (all_links_has_capacity) {
                    /* the path has enough capacity */
                        free_slot = match_path.slot_taken;
                        old_path = match_path;
                        is2Path = false;
                        return true;
                    }

                }
            }

            //now 2 path of 50s
            for(int i = 0; i < pathList_L.size();i++) {
                Path path = pathList_L.get(i);
                if (path.getSourceID() == p.getSourceID() && path.getDestNodeID() == p.getDestNodeID()) {
                    match_path = path;
                    /* we have a matching path, now see if capacity is available */
                    Vector<Link> links = match_path.getLinks();
                    boolean all_links_has_capacity = true;
                    for (int j = 0; j < links.size(); j++) {
                        int src = links.get(j).startNodeID;
                        int dst = links.get(j).endNodeID;
                        Link l = (Link) testTopo.adjMatrix[src][dst].get(1);

                        if (l.free_capacity_l.get(match_path.slot_taken) < 50) {
                            all_links_has_capacity = false;
                        }
                    }
                    if (all_links_has_capacity) {
                        /* the path has enough capacity */
                        free_slot = match_path.slot_taken;
                        old_path = match_path;

                        //find second path with another 50
                        for (int j = i + 1; j < pathList_L.size(); j++) {
                            Path path_2nd = pathList_L.get(j);
                            if (path_2nd.modulation != match_path.modulation) {
                                continue;
                            } else {

                                Vector<Link> links_2nd = path_2nd.getLinks();
                                Vector<Link> links_match_path = match_path.getLinks();

                                boolean same_path = false;
                                if (links_2nd.size() != links_match_path.size()) {
                                    same_path = false;
                                } else {
                                    same_path = true;
                                    for (int k = 0; k < links_2nd.size(); k++) {
                                        if (links_2nd.get(k).startNodeID == links_match_path.get(k).startNodeID && links_2nd.get(k).endNodeID == links_match_path.get(k).endNodeID) {
                                            continue;
                                        } else {
                                            same_path = false;
                                            break;
                                        }
                                    }

                                }
                                Link l_2 = null;
                                if (same_path) {
                                    boolean all_links_has_capacity_2 = true;

                                    for (int k = 0; k < links_2nd.size(); k++) {
                                        int src = links_2nd.get(k).startNodeID;
                                        int dst = links_2nd.get(k).endNodeID;
                                        l_2 = (Link) testTopo.adjMatrix[src][dst].get(1);

                                        if (l_2.free_capacity_l.get(path_2nd.slot_taken) < 50) {
                                            all_links_has_capacity_2 = false;
                                        }
                                    }

                                    if (all_links_has_capacity_2) {
                                        free_slot_2nd = path_2nd.slot_taken;
                                        old_path_2nd = path_2nd;
                                        if(free_slot==free_slot_2nd)
                                            continue;
                                        //System.out.println(old_path.toString());
                                        //System.out.println("check " + free_slot + " "+ free_slot_2nd + " " + l_2.free_capacity_l.get(free_slot_2nd));
                                        is2Path = true;
                                        return true;
                                    }
                                }
                            }
                        }
                    }

                }
            }


            return false;
        }

        public static boolean check_bandwidth_availibility_L(Path p) {
            Vector<Link> links = p.getLinks();
            Double path_ase = 0.0;
            Double path_osnr_mw = 0.0;
            Double path_noise = 0.0;


        /* calculate OSNR on shortest path */
            for (int i = 0; i < links.size(); i++) {
                int src = links.get(i).startNodeID;
                int dst = links.get(i).endNodeID;
                double link_ase = read_xl(src+"_"+dst,dst+"_"+src, Constants.osnr_file_L);
                path_ase += link_ase;
                //System.out.print("src: " + src + "dst: " + dst + "path_ase: " + path_ase);
            }

            path_noise = path_ase + (links.size()+1) * Constants.node_ase;
            path_osnr_mw = Constants.launch_power/path_noise;
            path_osnr_db = 10*Math.log10(path_osnr_mw);

            int modulation_type = osnr_thrsld_table(path_osnr_db);

            if(modulation_type == 0){
                int links_with_free_slots = 0;
                for (int i = 0; i < Constants.channels_perLink-1; i++) {
                    for (int j = 0; j < links.size(); j++) {
                        int src = links.get(j).startNodeID;
                        int dst = links.get(j).endNodeID;
                        Link l = (Link) testTopo.adjMatrix[src][dst].get(1);
                        if (l.slot_occupation_l.get(i) == 0 && l.slot_occupation_l.get(i+1)==0) {
                            links_with_free_slots +=1;
                        }
                        else{
                            links_with_free_slots = 0;
                            break;
                        }
                    }
                    if(links_with_free_slots == links.size()) {
                        free_slot = i;
                        //System.out.println(free_slot);
                        return true;
                    }
                }
                return false;
            }

            else {

                int links_with_free_slots = 0;
                for (int i = 0; i < Constants.channels_perLink; i++) {
                    for (int j = 0; j < links.size(); j++) {
                        int src = links.get(j).startNodeID;
                        int dst = links.get(j).endNodeID;
                        Link l = (Link) testTopo.adjMatrix[src][dst].get(1);
                        if (l.slot_occupation_l.get(i) == 0) {
                            links_with_free_slots += 1;
                        } else {
                            links_with_free_slots = 0;
                            break;
                        }
                    }
                    if (links_with_free_slots == links.size()) {
                        free_slot = i;
                        //System.out.println(free_slot);
                        return true;
                    }
                }
            }

            return false;
        }

        //0:BPSK 1:QPSK  2:8QAM 3:16QAM 4:32QAM 5:64QAM
        public static int osnr_thrsld_table(double path_osnr_db){
            if (path_osnr_db>=9 && path_osnr_db <12){
                capacity = 50;
                return 0;
            }
            else if (path_osnr_db>=12&& path_osnr_db <16){
                capacity = 100;
                return 1;
            }
            else if (path_osnr_db>=16 && path_osnr_db <18.6){
                capacity = 150;
                return 2;
            }
            else if (path_osnr_db>=18.6 && path_osnr_db <21.6){
                capacity = 200;
                return 3;
            }
            else if (path_osnr_db>=21.6 && path_osnr_db < 24.6){
                capacity = 250;
                return 4;
            }
            else if(path_osnr_db>=24.6) {
                capacity = 300;
                return 5;
            }

            // capacity = 50;
            return 0;

        }

        public static double read_xl(String link_info1, String link_info2, String name){
            try {

                FileReader f_read = new FileReader(name);
                BufferedReader b_read = new BufferedReader(f_read);
                String line;
                while ((line = b_read.readLine()) != null){
                    String[] split = line.split(",");
                    String link = split[0];
                    // System.out.println(link + " " + link_info1);
                    if (link.equals(link_info1) || link.equals(link_info2)) {
                        //System.out.println(link);
                        b_read.close();
                        f_read.close();
                        return Double.parseDouble(split[1]);
                    }
                }
                b_read.close();
                f_read.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }

        //path_type = 0: old path;   1: new path  2: 2 paths 8qam/32qam
        public static void subtract_bandwidth_availibility_L(Path p, int path_type, boolean isExisting) {


            //Calculate Modulation and capacity from path OSNR
            int modulation = 0;
            if(path_type==1) { // new path
                modulation = osnr_thrsld_table(path_osnr_db);
                // System.out.println(modulation + " " + capacity);
            }
            else if(path_type==0) //old path
                modulation = (int) p.modulation; //osnr_thrsld_table(p.path_osnr_db);
            else if (path_type==2) // old 2 path
                modulation = (int) p.modulation;

            if(modulation == 0){
                mod_bpsk++;}
            else if (modulation == 1){
                mod_qpsk++;
            }
            else if(modulation == 2){
                mod_8qam++;
            }
            else if(modulation == 3){
                mod_16qam++;
            }
            else if(modulation == 4){
                mod_32qam++;
            }
            else if(modulation == 5){
                mod_64qam++;
            }

            //special case: 2 paths 8QAM & 32QAM
            if(path_type == 2){
                //System.out.println(p.modulation + " " + old_path_2nd.modulation);
                Vector<Link> links = old_path.getLinks();
                Vector<Link> links_2nd = old_path_2nd.getLinks();

                //System.out.println(old_path.toString());

                for (int i = 0; i < links.size(); i++) {
                    int src = links.get(i).startNodeID;
                    int dst = links.get(i).endNodeID;
                    Link l = (Link) testTopo.adjMatrix[src][dst].get(1);
                    l.slot_occupation_l.set(free_slot, 1);
                    l.free_capacity_l.set(free_slot,l.free_capacity_l.get(free_slot)-50.0);
                    if(l.free_capacity_l.get(free_slot)<0)
                        //System.out.println(" 1st " + free_slot);
                        //System.out.println(p.toString());
                        //2nd path
                        src = links_2nd.get(i).startNodeID;
                    dst = links_2nd.get(i).endNodeID;
                    Link l_2 = (Link) testTopo.adjMatrix[src][dst].get(1);
                    l_2.slot_occupation_l.set(free_slot_2nd, 1);
                    //if(l_2.free_capacity_l.get(free_slot_2nd) < 50)
                    // System.out.println(" 2nd " + free_slot + " " + free_slot_2nd + " " + l_2.free_capacity_l.get(free_slot_2nd));

                    l_2.free_capacity_l.set(free_slot_2nd,l_2.free_capacity_l.get(free_slot_2nd)-50.0);

//                if(l_2.free_capacity_c.get(free_slot_2nd)<0)
//                    System.out.println(" 2nd " + free_slot_2nd + " " + l_2.free_capacity_c.get(free_slot_2nd));
//                //System.out.println(old_path_2nd.toString());
                }
            }
            //one old path
            else if (path_type==0){
                Vector<Link> links = p.getLinks();
                for (int i = 0; i < links.size(); i++) {
                    int src = links.get(i).startNodeID;
                    int dst = links.get(i).endNodeID;
                    Link l = (Link) testTopo.adjMatrix[src][dst].get(1);
                    l.slot_occupation_l.set(free_slot, 1);

                    //if ((l.free_capacity_l.get(free_slot) - 100.0) < 0)
                    //System.out.println("one old path " + modulation + " " + l.free_capacity_l.get(free_slot));
                    l.free_capacity_l.set(free_slot, l.free_capacity_l.get(free_slot) - 100.0);
                }

            }
            //all new path
            else if (path_type==1) {
                Vector<Link> links = p.getLinks();
                if (modulation == 0) {
                    //2 new slots for BPSK
                    for (int i = 0; i < links.size(); i++) {
                        int src = links.get(i).startNodeID;
                        int dst = links.get(i).endNodeID;
                        Link l = (Link) testTopo.adjMatrix[src][dst].get(1);
                        l.slot_occupation_l.set(free_slot, 1);
                        l.slot_occupation_l.set(free_slot + 1, 1);
                        l.free_capacity_l.set(free_slot, 0.0);
                        l.free_capacity_l.set(free_slot + 1, 0.0);

                    }
                } else {
                    for (int i = 0; i < links.size(); i++) {
                        int src = links.get(i).startNodeID;
                        int dst = links.get(i).endNodeID;
                        Link l = (Link) testTopo.adjMatrix[src][dst].get(1);
                        l.slot_occupation_l.set(free_slot, 1);
                        l.free_capacity_l.set(free_slot, capacity - 100.0);
                        //if((capacity - 100.0)<0)
                        //System.out.println("new path " + modulation + " " + (capacity));
                    }
                }
            }
            l_modulation = modulation;

            if(!isExisting) {
                p.slot_taken = free_slot;
                p.path_osnr_db = path_osnr_db;
                p.modulation = modulation;

                pathList_L.add(p);
                new_lightpath_L++;
            }

            request_inL++;

        }



        //***********************************     For C, Check Lightpath Capacity   *********************************************
        public static boolean check_existing_lightpath_C(Path p){
        /* Check source-dst */
            /****** case where there is already a lightpath ******/

            Path match_path;
            for(int i = 0; i < pathList_C.size();i++){
                Path path = pathList_C.get(i);
                if(path.getSourceID()==p.getSourceID() && path.getDestNodeID()==p.getDestNodeID()){
                    match_path = path;
                /* we have a matching path, now see if capacity is available */
                    Vector<Link> links = match_path.getLinks();
                    boolean all_links_has_capacity = true;
                    for(int j = 0; j<links.size(); j++) {
                        int src = links.get(j).startNodeID;
                        int dst = links.get(j).endNodeID;
                        Link l = (Link) testTopo.adjMatrix[src][dst].get(1);

                        if (l.free_capacity_c.get(match_path.slot_taken) < 100) {
                            all_links_has_capacity = false;
                        }
                    }
                    if (all_links_has_capacity) {
                    /* the path has enough capacity */
                        free_slot = match_path.slot_taken;
                        old_path = match_path;
                        is2Path = false;
                        return true;
                    }

                }
            }

            //now 2 path of 50s
            for(int i = 0; i < pathList_C.size();i++) {
                Path path = pathList_C.get(i);
                if (path.getSourceID() == p.getSourceID() && path.getDestNodeID() == p.getDestNodeID()) {
                    match_path = path;
                    /* we have a matching path, now see if capacity is available */
                    Vector<Link> links = match_path.getLinks();
                    boolean all_links_has_capacity = true;
                    for (int j = 0; j < links.size(); j++) {
                        int src = links.get(j).startNodeID;
                        int dst = links.get(j).endNodeID;
                        Link l = (Link) testTopo.adjMatrix[src][dst].get(1);

                        if (l.free_capacity_c.get(match_path.slot_taken) < 50) {
                            all_links_has_capacity = false;
                        }
                    }
                    if (all_links_has_capacity) {
                        /* the path has enough capacity */
                        free_slot = match_path.slot_taken;
                        old_path = match_path;

                        //find second path with another 50
                        for (int j = i + 1; j < pathList_C.size(); j++) {
                            Path path_2nd = pathList_C.get(j);
                            if (path_2nd.modulation != match_path.modulation) {
                                continue;
                            } else {

                                Vector<Link> links_2nd = path_2nd.getLinks();
                                Vector<Link> links_match_path = match_path.getLinks();

                                boolean same_path = false;
                                if (links_2nd.size() != links_match_path.size()) {
                                    same_path = false;
                                } else {
                                    same_path = true;
                                    for (int k = 0; k < links_2nd.size(); k++) {
                                        if (links_2nd.get(k).startNodeID == links_match_path.get(k).startNodeID && links_2nd.get(k).endNodeID == links_match_path.get(k).endNodeID) {
                                            continue;
                                        } else {
                                            same_path = false;
                                            break;
                                        }
                                    }

                                }
                                Link l_2 = null;
                                if (same_path) {
                                    boolean all_links_has_capacity_2 = true;

                                    for (int k = 0; k < links_2nd.size(); k++) {
                                        int src = links_2nd.get(k).startNodeID;
                                        int dst = links_2nd.get(k).endNodeID;
                                        l_2 = (Link) testTopo.adjMatrix[src][dst].get(1);

                                        if (l_2.free_capacity_c.get(path_2nd.slot_taken) < 50) {
                                            all_links_has_capacity_2 = false;
                                        }
                                    }

                                    if (all_links_has_capacity_2) {
                                        free_slot_2nd = path_2nd.slot_taken;
                                        old_path_2nd = path_2nd;
                                        if(free_slot==free_slot_2nd)
                                            continue;
                                        // System.out.println(old_path.toString());
                                        //System.out.println("check " + free_slot + " "+ free_slot_2nd + " " + l_2.free_capacity_c.get(free_slot_2nd));
                                        is2Path = true;
                                        return true;
                                    }
                                }
                            }
                        }
                    }

                }
            }


            return false;
        }

    public static boolean check_bandwidth_availibility_C(Path p) {
        Vector<Link> links = p.getLinks();
        Double path_ase = 0.0;
        Double path_osnr_mw = 0.0;
        Double path_noise = 0.0;


        /* calculate OSNR on shortest path */
        for (int i = 0; i < links.size(); i++) {
            int src = links.get(i).startNodeID;
            int dst = links.get(i).endNodeID;
           // double link_ase = read_xl(src+"_"+dst,dst+"_"+src, Constants.osnr_file_C);
           // path_ase += link_ase;
            Link l = testTopo.getLink(src, dst);
            if(isLink_inList(l,links_over_80)) {
                //  System.out.println(l.toString());
                double link_ase = read_xl(src + "_" + dst, dst + "_" + src, Constants.osnr_file_C);
                //  System.out.println(link_ase + " " + l.toString());
                path_ase += link_ase;
            }
            else {
                double link_ase = read_xl(src + "_" + dst, dst + "_" + src, Constants.osnr_file_c);
                // System.out.println(link_ase + " " + l.toString());
                path_ase += link_ase;
            }

        }

        path_noise = path_ase + (links.size()+1) * Constants.node_ase;
        path_osnr_mw = Constants.launch_power/path_noise;
        path_osnr_db = 10*Math.log10(path_osnr_mw);

        int modulation_type = osnr_thrsld_table(p.path_osnr_db);

        if(modulation_type == 0){
            int links_with_free_slots = 0;
            for (int i = 0; i < Constants.channels_perLink-1; i++) {
                for (int j = 0; j < links.size(); j++) {
                    int src = links.get(j).startNodeID;
                    int dst = links.get(j).endNodeID;
                    Link l = (Link) testTopo.adjMatrix[src][dst].get(1);
                    if (l.slot_occupation_c.get(i) == 0 && l.slot_occupation_c.get(i+1)==0) {
                        links_with_free_slots +=1;
                    }
                    else{
                        links_with_free_slots = 0;
                        break;
                    }
                }
                if(links_with_free_slots == links.size()) {
                    free_slot = i;
                    return true;
                }
            }
            return false;
        }


        /* check bandwidth for C */
        int links_with_free_slots = 0;
        for (int i = 0; i < Constants.channels_perLink; i++) {
            for (int j = 0; j < links.size(); j++) {
                int src = links.get(j).startNodeID;
                int dst = links.get(j).endNodeID;
                Link l = (Link) testTopo.adjMatrix[src][dst].get(1);
                if (l.slot_occupation_c.get(i) == 0) {
                    links_with_free_slots +=1;
                }
                else{
                    links_with_free_slots = 0;
                    break;
                }
            }
            if(links_with_free_slots == links.size()) {
                free_slot = i;
                return true;
            }
        }
        return false;
    }


    //path_type = 0: old path;   1: new path  2: 2 paths 8qam/32qam
        public static void subtract_bandwidth_availibility_C(Path p, int path_type, boolean isExisting) {

            //Calculate Modulation and capacity from path OSNR
            int modulation = 0;
            if(path_type==1) { // new path
                modulation = osnr_thrsld_table(path_osnr_db);
                // System.out.println(modulation + " " + capacity);
            }
            else if(path_type==0) //old path
                modulation = (int) p.modulation; //osnr_thrsld_table(p.path_osnr_db);
            else if (path_type==2) // old 2 path
                modulation = (int) p.modulation;

            if(modulation == 0){
                mod_bpsk++;}
            else if (modulation == 1){
                mod_qpsk++;
            }
            else if(modulation == 2){
                mod_8qam++;
            }
            else if(modulation == 3){
                mod_16qam++;
            }
            else if(modulation == 4){
                mod_32qam++;
            }
            else if(modulation == 5){
                mod_64qam++;
            }

            //special case: 2 paths 8QAM & 32QAM
            if(path_type == 2){
                //System.out.println(p.modulation + " " + old_path_2nd.modulation);
                Vector<Link> links = old_path.getLinks();
                Vector<Link> links_2nd = old_path_2nd.getLinks();

                //System.out.println(old_path.toString());

                for (int i = 0; i < links.size(); i++) {
                    int src = links.get(i).startNodeID;
                    int dst = links.get(i).endNodeID;
                    Link l = (Link) testTopo.adjMatrix[src][dst].get(1);
                    l.slot_occupation_c.set(free_slot, 1);
                    l.free_capacity_c.set(free_slot,l.free_capacity_c.get(free_slot)-50.0);
                    //if(l.free_capacity_c.get(free_slot)<0)
                    //System.out.println(" 1st " + free_slot);
                    //System.out.println(p.toString());
                    //2nd path
                    src = links_2nd.get(i).startNodeID;
                    dst = links_2nd.get(i).endNodeID;
                    Link l_2 = (Link) testTopo.adjMatrix[src][dst].get(1);
                    l_2.slot_occupation_c.set(free_slot_2nd, 1);
                    //if(l_2.free_capacity_c.get(free_slot_2nd) < 50)
                    // System.out.println(" 2nd " + free_slot + " " + free_slot_2nd + " " + l_2.free_capacity_c.get(free_slot_2nd));

                    l_2.free_capacity_c.set(free_slot_2nd,l_2.free_capacity_c.get(free_slot_2nd)-50.0);

//                if(l_2.free_capacity_c.get(free_slot_2nd)<0)
//                    System.out.println(" 2nd " + free_slot_2nd + " " + l_2.free_capacity_c.get(free_slot_2nd));
//                //System.out.println(old_path_2nd.toString());
                }
            }
            //one old path
            if (path_type==0){
                Vector<Link> links = p.getLinks();
                for (int i = 0; i < links.size(); i++) {
                    int src = links.get(i).startNodeID;
                    int dst = links.get(i).endNodeID;
                    Link l = (Link) testTopo.adjMatrix[src][dst].get(1);
                    l.slot_occupation_c.set(free_slot, 1);
                    //if ((l.free_capacity_c.get(free_slot) - 100.0) < 0)
                    //System.out.println("one old path " + modulation + " " + l.free_capacity_c.get(free_slot));
                    l.free_capacity_c.set(free_slot, l.free_capacity_c.get(free_slot) - 100.0);
                }
            }
            //all new path
            else if (path_type==1) {
                Vector<Link> links = p.getLinks();
                if (modulation == 0) {
                    //2 new slots for BPSK
                    for (int i = 0; i < links.size(); i++) {
                        int src = links.get(i).startNodeID;
                        int dst = links.get(i).endNodeID;
                        Link l = (Link) testTopo.adjMatrix[src][dst].get(1);
                        l.slot_occupation_c.set(free_slot, 1);
                        l.slot_occupation_c.set(free_slot + 1, 1);
                        l.free_capacity_c.set(free_slot, 0.0);
                        l.free_capacity_c.set(free_slot + 1, 0.0);
                    }
                } else {
                    for (int i = 0; i < links.size(); i++) {
                        int src = links.get(i).startNodeID;
                        int dst = links.get(i).endNodeID;
                        Link l = (Link) testTopo.adjMatrix[src][dst].get(1);
                        l.slot_occupation_c.set(free_slot, 1);
                        l.free_capacity_c.set(free_slot, capacity - 100.0);
                        //if((capacity - 100.0)<0)
                        //   System.out.println("new path " + modulation + " " + (capacity));
                    }
                }
            }

            if(!isExisting) {
                p.slot_taken = free_slot;
                p.path_osnr_db = path_osnr_db;
                p.modulation = modulation;
                pathList_C.add(p);
                new_lightpath_C++;
            }

        }




































    /***************************************** high util + high node + Joint 2 upgrades *********************************************/
//    public static void pre_update(int req_num) {
//        if (req_num == (920)) {
//            Link l = testTopo.getLink(5, 21);
//            links_over_80.add(l);
//            l = testTopo.getLink(5, 18);
//            links_over_80.add(l);
//            l = testTopo.getLink(10, 12);
//            links_over_80.add(l);
//            l = testTopo.getLink(11, 21);
//            links_over_80.add(l);
//            l = testTopo.getLink(4, 12);
//            links_over_80.add(l);
//            l = testTopo.getLink(6, 10);
//            links_over_80.add(l);
//            l = testTopo.getLink(10, 21);
//            links_over_80.add(l);
//            l = testTopo.getLink(2, 17);
//            links_over_80.add(l);
//            l = testTopo.getLink(2, 4);
//            links_over_80.add(l);
//            l = testTopo.getLink(16, 18);
//            links_over_80.add(l);
//            l = testTopo.getLink(5, 13);
//            links_over_80.add(l);
//            l = testTopo.getLink(6, 19);
//            links_over_80.add(l);
//            l = testTopo.getLink(11, 20);
//            links_over_80.add(l);
//            l = testTopo.getLink(0, 18);
//            links_over_80.add(l);
//            l = testTopo.getLink(4, 13);
//            links_over_80.add(l);
//            l = testTopo.getLink(8, 18);
//            links_over_80.add(l);
//            l = testTopo.getLink(0, 17);
//            links_over_80.add(l);
//            l = testTopo.getLink(1, 13);
//            links_over_80.add(l);
//        }
//    }

    /***************************************** high node 2 upgrades *********************************************/
    /*public static void pre_update(int req_num) {
        if (req_num == (880)) {
            Link l = testTopo.getLink(5, 21);
            links_over_80.add(l);
            l = testTopo.getLink(4, 12);
            links_over_80.add(l);
            l = testTopo.getLink(5, 18);
            links_over_80.add(l);
            l = testTopo.getLink(10, 12);
            links_over_80.add(l);
            l = testTopo.getLink(2, 4);
            links_over_80.add(l);
            l = testTopo.getLink(6, 10);
            links_over_80.add(l);
            l = testTopo.getLink(2, 17);
            links_over_80.add(l);
            l = testTopo.getLink(11, 21);
            links_over_80.add(l);
            l = testTopo.getLink(4, 13);
            links_over_80.add(l);
            l = testTopo.getLink(16, 18);
            links_over_80.add(l);
            l = testTopo.getLink(10, 21);
            links_over_80.add(l);
            l = testTopo.getLink(5, 13);
            links_over_80.add(l);
            l = testTopo.getLink(6, 19);
            links_over_80.add(l);
            l = testTopo.getLink(11, 20);
            links_over_80.add(l);
            l = testTopo.getLink(0, 17);
            links_over_80.add(l);
            l = testTopo.getLink(1, 13);
            links_over_80.add(l);
            l = testTopo.getLink(3, 9);
            links_over_80.add(l);
            l = testTopo.getLink(7, 20);
            links_over_80.add(l);
        }
    }*/

/***************************************** Joint probability 2 upgrades *********************************************/
       /* public static void pre_update(int req_num) {
            if (req_num == (880)) {
                Link l = testTopo.getLink(2, 17);
                links_over_80.add(l);
                l = testTopo.getLink(4, 12);
                links_over_80.add(l);
                l = testTopo.getLink(5, 18);
                links_over_80.add(l);
                l = testTopo.getLink(5, 21);
                links_over_80.add(l);
                l = testTopo.getLink(2, 4);
                links_over_80.add(l);
                l = testTopo.getLink(10, 12);
                links_over_80.add(l);
                l = testTopo.getLink(16, 18);
                links_over_80.add(l);
                l = testTopo.getLink(5, 13);
                links_over_80.add(l);
                l = testTopo.getLink(6, 10);
                links_over_80.add(l);
                l = testTopo.getLink(11, 20);
                links_over_80.add(l);
                l = testTopo.getLink(11, 21);
                links_over_80.add(l);
                l = testTopo.getLink(16, 17);
                links_over_80.add(l);
                l = testTopo.getLink(0, 18);
                links_over_80.add(l);
                l = testTopo.getLink(4, 13);
                links_over_80.add(l);
                l = testTopo.getLink(6, 11);
                links_over_80.add(l);
                l = testTopo.getLink(7, 9);
                links_over_80.add(l);
                l = testTopo.getLink(7, 20);
                links_over_80.add(l);
                l = testTopo.getLink(10, 21);
                links_over_80.add(l);
            }
        }*/

    /******************************************    betweeness centrality  2 points  *********************************/
    /*public static void pre_update(int req_num) {

        if (req_num == (880)) {
            Link l = testTopo.getLink(5, 21);
            links_over_80.add(l);
            l = testTopo.getLink(5, 18);
            links_over_80.add(l);
            l = testTopo.getLink(3, 9);
            links_over_80.add(l);
            l = testTopo.getLink(4, 12);
            links_over_80.add(l);
            l = testTopo.getLink(11, 21);
            links_over_80.add(l);
            l = testTopo.getLink(2, 17);
            links_over_80.add(l);
            l = testTopo.getLink(7, 9);
            links_over_80.add(l);
            l = testTopo.getLink(10, 12);
            links_over_80.add(l);
            l = testTopo.getLink(2, 4);
            links_over_80.add(l);
            l = testTopo.getLink(10, 21);
            links_over_80.add(l);
            l = testTopo.getLink(4, 13);
            links_over_80.add(l);
            l = testTopo.getLink(16, 18);
            links_over_80.add(l);
            l = testTopo.getLink(5, 13);
            links_over_80.add(l);
            l = testTopo.getLink(6, 10);
            links_over_80.add(l);
            l = testTopo.getLink(0, 17);
            links_over_80.add(l);
            l = testTopo.getLink(7, 20);
            links_over_80.add(l);
            l = testTopo.getLink(1, 13);
            links_over_80.add(l);

            }
            }*/
    /***************************************** Link utilization 2 upgrades *********************************************/
        /*public static void pre_update(int req_num) {
            if (req_num == (880)) {
                Link l = testTopo.getLink(5, 21);
                links_over_80.add(l);
                l = testTopo.getLink(5, 18);
                links_over_80.add(l);
                l = testTopo.getLink(4, 12);
                links_over_80.add(l);
                l = testTopo.getLink(11, 21);
                links_over_80.add(l);
                l = testTopo.getLink(2, 4);
                links_over_80.add(l);
                l = testTopo.getLink(16, 18);
                links_over_80.add(l);
                l = testTopo.getLink(2, 17);
                links_over_80.add(l);
                l = testTopo.getLink(6, 10);
                links_over_80.add(l);
                l = testTopo.getLink(10, 12);
                links_over_80.add(l);
                l = testTopo.getLink(5, 13);
                links_over_80.add(l);
                l = testTopo.getLink(4, 13);
                links_over_80.add(l);
                l = testTopo.getLink(3, 9);
                links_over_80.add(l);
                l = testTopo.getLink(11, 20);
                links_over_80.add(l);
                l = testTopo.getLink(7, 9);
                links_over_80.add(l);
                l = testTopo.getLink(10, 21);
                links_over_80.add(l);
                l = testTopo.getLink(7, 20);
                links_over_80.add(l);
                l = testTopo.getLink(16, 17);
                links_over_80.add(l);

            }

            else if (req_num == (1340)) {
                Link l = testTopo.getLink(2, 3);
                links_over_80.add(l);
                l = testTopo.getLink(6, 11);
                links_over_80.add(l);
                l = testTopo.getLink(7, 10);
                links_over_80.add(l);
                l = testTopo.getLink(1, 13);
                links_over_80.add(l);
                l = testTopo.getLink(3, 15);
                links_over_80.add(l);
                l = testTopo.getLink(0, 17);
                links_over_80.add(l);
                l = testTopo.getLink(9, 12);
                links_over_80.add(l);
                l = testTopo.getLink(14, 15);
                links_over_80.add(l);
                l = testTopo.getLink(2, 15);
                links_over_80.add(l);
                l = testTopo.getLink(14, 16);
                links_over_80.add(l);
                l = testTopo.getLink(8, 18);
                links_over_80.add(l);
                l = testTopo.getLink(0, 18);
                links_over_80.add(l);
                l = testTopo.getLink(0, 1);
                links_over_80.add(l);
                l = testTopo.getLink(19, 20);
                links_over_80.add(l);
                l = testTopo.getLink(1, 8);
                links_over_80.add(l);
                l = testTopo.getLink(6, 19);
                links_over_80.add(l);
                l = testTopo.getLink(14, 17);
                links_over_80.add(l);
                l = testTopo.getLink(0, 8);
                links_over_80.add(l);

            }
        }*/


        public static double spectrum_occupancy_c(Link l){
            Double spectrum_occupied= 0.0;
            for (int i=0; i< Constants.channels_perLink; i++){
                if(l.slot_occupation_c.get(i) == 1){
                    spectrum_occupied += 300 - l.free_capacity_c.get(i);

                }
                else spectrum_occupied+= 0;

            }
            return (spectrum_occupied/(Constants.channels_perLink*300*2));
        }
    public static double spectrum_occupancy_cplusl(Link l){
        Double spectrum_occupied= 0.0;
        for (int i=0; i< Constants.channels_perLink; i++){
            if(l.slot_occupation_c.get(i) == 1){
                spectrum_occupied += 300 - l.free_capacity_c.get(i);

            }
            else spectrum_occupied+= 300;

        }
        for (int i=0; i< Constants.channels_perLink; i++){
            if(l.slot_occupation_l.get(i) == 1){
                spectrum_occupied += 300 - l.free_capacity_l.get(i);

            }
            else spectrum_occupied+= 300;

        }
        return (spectrum_occupied/(Constants.channels_perLink*300*2));
    }








































///**************************************************//

        public static void print_path(Path p) {
            Vector<Link> links = p.getLinks();
            for (int i = 0; i < links.size(); i++) {
                int src = links.get(i).startNodeID;
                int dst = links.get(i).endNodeID;
                Link l = (Link) testTopo.adjMatrix[src][dst].get(1);
                System.out.println(l.getStartNodeID() + ", " + l.getEndNodeID() + ", " + l.avail_capacity);
            }
        }

        public static double avg_cal(ArrayList<Double> list) {
            double sum = 0.0;
            for (int i = 0; i < list.size(); i++) {
                sum += list.get(i);
            }
            return sum / list.size();
        }

        public static double err_up_cal(ArrayList<Double> list, double avg) {
            double err = 0.0;
            int count = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) >= avg) {
                    err += list.get(i) - avg;
                    count++;
                }
            }
            return err / count;
        }

        public static double err_down_cal(ArrayList<Double> list, double avg) {
            double err = 0.0;
            int count = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) < avg) {
                    err += avg - list.get(i);
                    count++;
                }
            }
            return err / count;
        }

        public static void handleStart_Short(Event ev) {
            Path short_path = new Path();


            boolean no_path = false;
            if ((int) testTopo.pathMatrix[ev.getSource()][ev.getDest()].get(0) > 0) {
                short_path = (Path) testTopo.pathMatrix[ev.getSource()][ev.getDest()].get(1);
            } else
                no_path = true;

            if (no_path) {
                blocked_100++;
                //System.out.println("blocked-no path: " + short_path.toString());
                return;
            }
            // System.out.println(short_path.toString());
            Boolean isBlocked=false;
        /* check for existing lightpath */
            Boolean isExisting = check_existing_lightpath_L(short_path);
            if (isExisting){
                subtract_bandwidth_availibility_L(old_path, 0, isExisting);
            }

            else {/* For new lightpath */
                if (!check_bandwidth_availibility_L(short_path)){
                    //blocked_100++;
                    isBlocked=true;
                    //System.out.println("Blocked-no available spectrum" + short_path.toString());
                    return;
                }
            /* Allocate the available capacity along the path */
                //1 is new path
                subtract_bandwidth_availibility_L(short_path,1, isExisting);
            }

        }

    }



