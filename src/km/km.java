package km;


public class km{
    int[][] graph;        //假设graph的行是顶点X集合（其中的顶点简称X顶点），列是顶点Y集合（其中的顶点简称Y顶点）
    boolean[] xUsed;      //在每次循环中每个X顶点是否访问过
    boolean[] yUsed;      //在每次循环中每个Y顶点是否访问过
    int[] match;          //每个Y顶点匹配的X顶点，即第i个Y顶点匹配的是第match[i]个X顶点
    int len;              //图的大小为len*len
    int[] less;           //与顶标变化相关
    private static final int INFINITE = 0x6fffffff;

    int[] X; //每个X顶点的顶标
    int[] Y; //每个Y顶点的顶标，初始化为0


    public km(int[][] data) {
        this.graph = data;
        len = data.length;
        xUsed = new boolean[len];
        yUsed = new boolean[len];
        match = new int[len];
        less = new int[len];

        X = new int[len];
        Y = new int[len];

        for (int i = 0; i < len; i++) {
            match[i] = -1;
        }

        //初始化每个X顶点的顶标为与之相连的边中最大的权
        for (int k = 0; k < len; k++) {
            X[k] = data[k][0];
            for (int l = 0; l < len; l++) {
                X[k] = X[k] >= data[k][l] ? X[k] : data[k][l];
            }
        }
    }

    int[] km() {
        //遍历每个X顶点
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                less[j] = INFINITE;
            }

            while (true) {   //寻找能与X顶点匹配的Y顶点，如果找不到就降低X顶点的顶标继续寻找
                for (int j = 0; j < len; j++) {
                    xUsed[j] = false;
                    yUsed[j] = false;
                }

                if (hungaryDFS(i)) break;  //寻找到匹配的Y顶点，退出

                //如果没有找到能够匹配的Y顶点，则降低X顶点的顶标，提升Y顶点的顶标，再次循环
                int diff = INFINITE;        //diff是顶标变化的数值
                for (int j = 0; j < len; j++) {
                    if (!yUsed[j]) diff = diff <= less[j] ? diff : less[j];
                }
                //diff等于为了使该顶点X能够匹配到一个Y顶点，其X的顶标所需要降低的最小值

                //更新顶标
                for (int j = 0; j < len; j++) {
                    if (xUsed[j]) X[j] -= diff;
                    if (yUsed[j]) Y[j] += diff;
                    else less[j] -= diff;
                }
            }
        }

        //匹配完成，可以输出结果
        int res = 0;
        for (int i = 0; i < len; i++) {
            res += graph[match[i]][i];
        }
        for (int i = 0; i < match.length; i++) {
			System.out.println("左边的："+match[i]+"-右边的："+i);
		}
        System.out.println("最终最大权值：" + res);
        return match;
    }

    private boolean hungaryDFS(int i) {
        //设置这个X顶点在此轮循环中被访问过
        xUsed[i] = true;

        //对于这个X顶点，遍历每个Y顶点
        for (int j = 0; j < len; j++) {
            if (yUsed[j]) continue;   //每轮循环中每个Y顶点只访问一次
            int gap = X[i] + Y[j] - graph[i][j];      //KM算法的顶标变化公式

            //只有X顶点的顶标加上Y顶点的顶标等于graph中它们之间的边的权时才能匹配成功
            if (gap == 0) {
                yUsed[j] = true;
                if (match[j] == -1 || hungaryDFS(match[j])) {
                    match[j] = i;
                    return true;
                }
            } else {
                less[j] = less[j] <= gap ? less[j] : gap;
            }
        }

        return false;
    }

    public static void main(String[] args) {
//        int[][] graph = {
//                {3, 0, 4},
//                {2, 1, 3},
//                {0, 0, 5}
//        };
//    	int[][] graph = {
//                {3, 3, 1, 13},
//                {1, 1, 1, 21},
//                {6, 4, 1, 24},
//                {0, 0, 0, 0}
//        };
    	int[][] graph = {
                {0, 0, 0},
                {0, 0, 0},
                {0, 1, 0}
        };
        new km(graph).km();
    }
}
