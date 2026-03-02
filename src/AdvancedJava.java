import java.util.*;

public class AdvancedJava {
    static HashMap<String,Integer> users = new HashMap<>();
    static HashMap<String,Integer> attempts = new HashMap<>();

    static boolean checkUsername(String name){
        attempts.put(name, attempts.getOrDefault(name,0)+1);
        return !users.containsKey(name);
    }

    static List<String> suggest(String name){
        List<String> list = new ArrayList<>();
        for(int i=1;i<=3;i++)
            list.add(name+i);
        list.add(name.replace("_","."));
        return list;
    }

    static String mostAttempted(){
        return attempts.entrySet().stream()
                .max(Map.Entry.comparingByValue()).get().getKey();
    }

    static HashMap<String,Integer> stock = new HashMap<>();
    static HashMap<String,Queue<Integer>> waitlist = new HashMap<>();

    static synchronized String buy(String product,int user){
        int s = stock.getOrDefault(product,0);
        if(s>0){
            stock.put(product,s-1);
            return "Purchased. Remaining "+(s-1);
        }
        waitlist.putIfAbsent(product,new LinkedList<>());
        waitlist.get(product).add(user);
        return "Out of stock. Added to waitlist position "+waitlist.get(product).size();
    }

    static class DNSEntry{
        String ip;
        long expiry;
        DNSEntry(String ip,int ttl){
            this.ip=ip;
            expiry=System.currentTimeMillis()+ttl*1000;
        }
    }
    static HashMap<String,DNSEntry> dnsCache = new HashMap<>();

    static String resolve(String domain){
        if(dnsCache.containsKey(domain)){
            DNSEntry e = dnsCache.get(domain);
            if(System.currentTimeMillis()<e.expiry)
                return "HIT → "+e.ip;
        }
        String ip="192.168."+new Random().nextInt(255);
        dnsCache.put(domain,new DNSEntry(ip,5));
        return "MISS → "+ip;
    }

    static HashMap<String,Set<String>> index = new HashMap<>();

    static void addDoc(String id,String text){
        String[] w=text.split(" ");
        for(int i=0;i<w.length-2;i++){
            String gram=w[i]+" "+w[i+1]+" "+w[i+2];
            index.putIfAbsent(gram,new HashSet<>());
            index.get(gram).add(id);
        }
    }

    static int similarity(String id,String text){
        String[] w=text.split(" ");
        int matches=0;
        for(int i=0;i<w.length-2;i++){
            String gram=w[i]+" "+w[i+1]+" "+w[i+2];
            if(index.containsKey(gram) && !index.get(gram).contains(id))
                matches++;
        }
        return matches;
    }

    static HashMap<String,Integer> views = new HashMap<>();
    static HashMap<String,Set<String>> unique = new HashMap<>();

    static void visit(String url,String user){
        views.put(url,views.getOrDefault(url,0)+1);
        unique.putIfAbsent(url,new HashSet<>());
        unique.get(url).add(user);
    }

    static class Bucket{
        int tokens;
        long last;
        Bucket(){tokens=5; last=System.currentTimeMillis();}
    }
    static HashMap<String,Bucket> buckets = new HashMap<>();

    static boolean allow(String id){
        buckets.putIfAbsent(id,new Bucket());
        Bucket b=buckets.get(id);

        long now=System.currentTimeMillis();
        if(now-b.last>5000){ b.tokens=5; b.last=now;}

        if(b.tokens>0){b.tokens--; return true;}
        return false;
    }


    static HashMap<String,Integer> queries = new HashMap<>();

    static List<String> search(String prefix){
        return queries.entrySet().stream()
                .filter(e->e.getKey().startsWith(prefix))
                .sorted((a,b)->b.getValue()-a.getValue())
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();
    }

    static String[] parking = new String[10];

    static int park(String plate){
        int h=Math.abs(plate.hashCode()%parking.length);
        int start=h;
        while(parking[h]!=null){
            h=(h+1)%parking.length;
            if(h==start) return -1;
        }
        parking[h]=plate;
        return h;
    }

    static void twoSum(int[] arr,int target){
        HashMap<Integer,Integer> map=new HashMap<>();
        for(int n:arr){
            if(map.containsKey(target-n)){
                System.out.println("Pair: "+n+" & "+(target-n));
                return;
            }
            map.put(n,1);
        }
    }

    static LinkedHashMap<String,String> L1 = new LinkedHashMap<>(5,0.75f,true){
        protected boolean removeEldestEntry(Map.Entry e){ return size()>5;}
    };
    static HashMap<String,String> L2 = new HashMap<>();

    static String getVideo(String id){
        if(L1.containsKey(id)) return "L1 HIT";
        if(L2.containsKey(id)){ L1.put(id,L2.get(id)); return "L2 HIT";}
        L2.put(id,"VideoData");
        return "DB HIT";
    }

    public static void main(String[] args){

        System.out.println("=== USERNAME CHECK ===");
        users.put("john",1);
        System.out.println(checkUsername("john"));
        System.out.println(suggest("john"));
        System.out.println();

        System.out.println("=== INVENTORY ===");
        stock.put("iphone",2);
        System.out.println(buy("iphone",1));
        System.out.println(buy("iphone",2));
        System.out.println(buy("iphone",3));
        System.out.println();

        System.out.println("=== DNS CACHE ===");
        System.out.println(resolve("google.com"));
        System.out.println(resolve("google.com"));
        System.out.println();

        System.out.println("=== PLAGIARISM ===");
        addDoc("doc1","java is very powerful language");
        System.out.println(similarity("doc2","java is very fast"));
        System.out.println();

        System.out.println("=== ANALYTICS ===");
        visit("/news","u1");
        visit("/news","u2");
        System.out.println(views);
        System.out.println(unique);
        System.out.println();

        System.out.println("=== RATE LIMIT ===");
        for(int i=0;i<7;i++)
            System.out.println(allow("client1"));
        System.out.println();

        System.out.println("=== AUTOCOMPLETE ===");
        queries.put("java tutorial",10);
        queries.put("javascript",7);
        queries.put("java download",5);
        System.out.println(search("jav"));
        System.out.println();

        System.out.println("=== PARKING ===");
        System.out.println(park("ABC123"));
        System.out.println(park("XYZ999"));
        System.out.println(Arrays.toString(parking));
        System.out.println();

        System.out.println("=== TWO SUM ===");
        twoSum(new int[]{100,300,200,400},500);
        System.out.println();

        System.out.println("=== CACHE ===");
        System.out.println(getVideo("v1"));
        System.out.println(getVideo("v1"));
    }
}