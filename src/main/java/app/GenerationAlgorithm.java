package app;

import core.role.Role;
import core.tasks.CircleTask;
import core.tasks.Task;

import java.util.*;

/**
 * @author SoonMachine
 */
public class GenerationAlgorithm {

    // 将任务从 0 开始编号，存储每个任务的长度
    int[] tasks;
    int TASK_NUM;


    int[] roles;
    int ROLE_NUM;

    // timeMatrix[i][j]：第 i 个任务放在处理器 j 上处理需要的时间
    double[][] timeMatrix;


    // adaptabilitu[i] 染色体 i 的适应度概率
    double[] adaptability;

    // selectionProbability[i]:第 i 个染色体的适应度概率
    double[] selectionProbability;

    int iteratorNum;
    int chromosomeNum;

    // 染色体复制比例
    double cp;



    public ArrayList<PII>  ga(ArrayList<Task> taskArrayList, ArrayList<Role> roleArrayList) {
        inputParam(taskArrayList,roleArrayList);
        inputData(taskArrayList,roleArrayList);
        initMatrix();
        ArrayList<PII> res;
        res = gaSearch(iteratorNum, chromosomeNum);
        return  res;
    }

    public void initMatrix() {
        // 任务-处理时间
        timeMatrix = new double[TASK_NUM][ROLE_NUM];

        for (int i = 0; i < TASK_NUM; i++) {
            for (int j = 0; j < ROLE_NUM; j++) {
                // 第i个任务放在处理器j上处理，所需时间
                timeMatrix[i][j] = (tasks[i] + 1.0) / roles[j];
            }
        }

        // 染色体适应度
        adaptability = new double[chromosomeNum];
        // 染色体适应度概率
        selectionProbability = new double[chromosomeNum];
    }

    /**
     * 输入任务序列、节点序列
     */
    public void inputData(ArrayList<Task> taskArrayList, ArrayList<Role> roleArrayList) {
        // 任务矩阵
        tasks = new int[TASK_NUM];
        // 节点矩阵
        roles = new int[ROLE_NUM];

        Random r = new Random();
        for (int i = 0; i < TASK_NUM; i++) {
            tasks[i] = taskArrayList.get(i).getMQPS().get("attack");
        }
        for (int i = 0; i < ROLE_NUM; i++) {
            roles[i] = roleArrayList.get(i).getCapabilities().get("attack");
        }

    }

    /**
     * 初始化相关参数
     */
    public void inputParam(ArrayList<Task> taskArrayList, ArrayList<Role> roleArrayList) {
        TASK_NUM = taskArrayList.size();
        ROLE_NUM = roleArrayList.size();
        // 迭代次数
        iteratorNum = 100;
        // 群体大小
        chromosomeNum = 10;

        // 染色体复制的比例(每代中保留适应度较高的染色体直接成为下一代)
        cp = 0.2;
    }





    /**
     * 计算群体中染色体的适应度概率
     * @param adaptability
     * @return
     */
    public double[] calSelectionProbability(double[] adaptability) {
        if (adaptability == null || adaptability.length == 0) {
            return null;
        }
        double[] selectionProb = new double[adaptability.length];
        // 总适应度
        double sum = 0;
        for (int i = 0; i < adaptability.length; i++) {
            sum += adaptability[i];
        }
        for (int i = 0; i < adaptability.length; i++) {
            selectionProb[i] = adaptability[i] / sum;
        }
        return selectionProb;
    }

    /**
     * 计算群体中染色体的适应度
     * @param chromosomeMatrix
     * @return
     */
    public double[] calAdaptability(List < int[] > chromosomeMatrix) {
        double[] adaptAbility = new double[chromosomeMatrix.size()];
        for (int i = 0; i < chromosomeMatrix.size(); i++) {
            // 对每一个染色体，计算总的处理时间
            int[] chromosome = chromosomeMatrix.get(i);
            double sum = getTotalProcessTime(chromosome);
            adaptAbility[i] = 1 / sum;
        }
        return adaptAbility;
    }

    /**
     * 生成每次迭代后的群体
     *
     * @param chromosomeMatrix:上一代群体
     * @param chromosomeNum：群体中染色体数目
     * @return
     */
    public List < int[] > createGeneration(List < int[] > chromosomeMatrix, int chromosomeNum) {
        Random r = new Random();

        if (chromosomeMatrix == null || chromosomeMatrix.size() == 0) {
            chromosomeMatrix = new ArrayList < > ();
            // 生成第一代染色体：随机生成染色体
            for (int i = 0; i < chromosomeNum; i++) {
                // 生成每个个体
                int[] chromosome = new int[TASK_NUM];
                HashSet<Integer> hashSet = new HashSet<>(TASK_NUM);
                for (int j = 0; j < TASK_NUM; j++) {

                    int temp = r.nextInt(ROLE_NUM);//[0,NODE_NUM)
                    while (hashSet.contains(temp)){
                        temp =  r.nextInt(ROLE_NUM);
                    }
                    hashSet.add(temp);
                    chromosome[j] = temp;
                }
                chromosomeMatrix.add(chromosome);
            }
            return chromosomeMatrix;
        } else {
            // 根据上一代群体生成本代群体
            // 一部分复制，另一份交叉-变异
            int copyNum = (int)(chromosomeNum * cp);
            List < int[] > crossChromosome = cross(chromosomeMatrix, chromosomeNum - copyNum);
            System.out.println("交叉产生的染色体个数：" + crossChromosome.size());
            // 从上一群体中复制的染色体
            List < int[] > copyChromosome = copy(chromosomeMatrix, copyNum);
            System.out.println("复制产生的染色体个数：" + copyChromosome.size());

            // 生成新一代染色体
            List < int[] > newChromosomeMatrix = new ArrayList < > ();
            newChromosomeMatrix.addAll(crossChromosome);
            newChromosomeMatrix.addAll(copyChromosome);
            System.out.println("新一代染色体个数：" + newChromosomeMatrix.size());

            return newChromosomeMatrix;
        }

    }

    /**
     * 从上一代群体chromosomeMatrix中复制产生 num 个染色体
     * @param chromosomeMatrix
     * @param num
     * @return
     */
    public List < int[] > copy(List < int[] > chromosomeMatrix, int num) {
        // 从上一代群体中找出适应度最高的num个染色体，把这些染色体复制到新的群体中
        return getMaxN(chromosomeMatrix, selectionProbability, num);
    }

    /**
     * 得到适应度最高的前num个染色体
     * @param chromosomeMatrix
     * @param selectionProbability
     * @param num
     * @return
     */
    public List < int[] > getMaxN(List < int[] > chromosomeMatrix, double[] selectionProbability, int num) {
        PriorityQueue < Chromosome > queue = new PriorityQueue < > (num);
        for (int i = 0; i < selectionProbability.length; i++) {
            if (queue.size() < num) {
                queue.add(new Chromosome(i, selectionProbability[i]));

            } else {
                Chromosome mincs = queue.peek();
                if (mincs.selectionProb < selectionProbability[i]) {
                    queue.poll();
                    queue.add(new Chromosome(i, selectionProbability[i]));
                }
            }
        }
        List < int[] > result = new ArrayList < > ();
        // 变量PriorityQueue
        if (queue != null && queue.size() != 0) {
            Iterator < Chromosome > iterator = queue.iterator();
            while (iterator.hasNext()) {
                Chromosome chromosome = iterator.next();
                result.add(chromosomeMatrix.get(chromosome.id));
            }
        }
        return result;
    }

    public static class Chromosome implements Comparable < Chromosome > {
        int id;
        double selectionProb;

        public Chromosome() {}

        public Chromosome(int id, double selectionProb) {
            this.id = id;
            this.selectionProb = selectionProb;
        }

        @Override
        public int compareTo(Chromosome o) {
            if (o == null) {
                return -1;
            }
            double r = this.selectionProb - o.selectionProb;
            if (r > 0) {
                return 1;
            } else if (r < 0) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    /**
     * 从上一代群体chromosomeMatrix中交叉-变异产生 num 个染色体
     *
     * @param chromosomeMatrix
     * @param num
     * @return
     */
    public List < int[] > cross(List < int[] > chromosomeMatrix, int num) {
        if (chromosomeMatrix == null || chromosomeMatrix.size() == 0) {
            return null;
        }
        Random r = new Random();
        List < int[] > crossChromosome = new ArrayList < > ();
        for (int i = 0; i < num; i++) {
            int[] chromosome = new int[TASK_NUM];

            // 采用轮盘赌算法获得爸爸和妈妈染色体
            int fatherIndex = getSelectedOne_byRoulette(selectionProbability);
            int motherIndex = getSelectedOne_byRoulette(selectionProbability);
            // 交叉位置

            int crossIndex = r.nextInt(TASK_NUM);
            int startIndex = r.nextInt(TASK_NUM);
            int endIndex = r.nextInt(TASK_NUM);
            if (startIndex > endIndex){
                int temp  = startIndex;
                startIndex = endIndex;
                endIndex = startIndex;
            }
            List<Integer> temp = new ArrayList<>();
            for (int j = 0; j < TASK_NUM; j++) {
                temp.add(-1);
            }
            int[] father = chromosomeMatrix.get(fatherIndex);
            int[] mother = chromosomeMatrix.get(motherIndex);
            for (int j = startIndex; j <= endIndex; j++) {
                temp.set(j,father[j]);
            }
            int index = 0;
            for (int j = 0; j < startIndex; j++) {
                int get = mother[index];
                while (temp.contains(get)){
                    index ++;
                    get = mother[index];
                }
                temp.set(j, get);
            }
            for (int j = endIndex + 1; j <  TASK_NUM; j++) {
                int get = mother[index];
                while (temp.contains(get)){
                    index ++;
                    get = mother[index];
                }
                temp.set(j, get);
            }


/*            System.arraycopy(father,startIndex,chromosome,startIndex,endIndex - startIndex + 1);

            System.arraycopy(chromosomeMatrix.get(fatherIndex), 0,
                    chromosome, 0, crossIndex + 1);
            System.arraycopy(chromosomeMatrix.get(motherIndex),
                    crossIndex + 1, chromosome, crossIndex + 1, TASK_NUM - crossIndex - 1);*/
            // 交叉后变异
            // 变异位置
            int mutationIndex = r.nextInt(TASK_NUM);
            int mutationIndex1 = r.nextInt(TASK_NUM);
            Collections.swap(temp,mutationIndex,mutationIndex1);
            for (int j = 0; j < TASK_NUM; j++) {
                chromosome[j] = temp.get(j);
            }

            crossChromosome.add(chromosome);
        }
        return crossChromosome;
    }


    /**
     * 根据轮盘赌算法，返回被选中的某个染色体的数组下标
     *
     * @param selectionProbability
     * @return
     */
    public int getSelectedOne_byRoulette(double[] selectionProbability) {
        Random random = new Random();
        double probTotal = random.nextDouble();
        double sum = 0.0;
        for (int i = 0; i < selectionProbability.length; i++) {
            sum += selectionProbability[i];
            if (sum >= probTotal) {
                return i;
            }
        }
        return 0;
    }


    /**
     * 遗传算法迭代
     *  @param iteratorNum
     * @param chromosomeNum
     * @return
     */
    public ArrayList<PII> gaSearch(int iteratorNum, int chromosomeNum) {
        // 初始化第一代群体
        List < int[] > chromosomeMatrix = createGeneration(null, chromosomeNum);
        // 迭代繁衍
        int[] ans = new int[0];
        double temp = Double.MAX_VALUE;
        for (int i = 2; i <= iteratorNum; i++) {
            // 计算上一代群体染色体的适应度
            adaptability = calAdaptability(chromosomeMatrix);
            //            System.out.println("迭代次数："+i+",计算上一代群体染色体的适应度:"+Arrays.toString(adaptability));
            // 计算上一代染色体的适应度概率
            selectionProbability = calSelectionProbability(adaptability);
            //            System.out.println("迭代次数："+i+",计算上一代染色体的适应度概率:"+Arrays.toString(selectionProbability));

            // 生成新一代染色体
            chromosomeMatrix = createGeneration(chromosomeMatrix, chromosomeNum);
            System.out.println("迭代次数：" + i + ",的每个染色体总评价指数：");
            for (int j = 0; j < chromosomeMatrix.size(); j++) {
                double totalProcessTime = getTotalProcessTime(chromosomeMatrix.get(j));
                if (temp > totalProcessTime){
                    temp = totalProcessTime;
                    ans = chromosomeMatrix.get(j);
                }
                System.out.println("评价指数：" + getTotalProcessTime(chromosomeMatrix.get(j)));
            }
        }
        ArrayList<PII> res = new ArrayList<>();
        for (int i = 0; i < ans.length; i++) {
            System.out.println("任务 " + i + " 分配给了角色 " + ans[i]);
            res.add(new PII(i,ans[i]));
        }
        return res;
    }

    /**
     * 得到一个染色体总的处理时间
     * @param chromosome
     * @return
     */
    private double getTotalProcessTime(int[] chromosome) {
        double sum = 0;
        for (int j = 0; j < chromosome.length; j++) {
            int nodeId = chromosome[j];
            sum += timeMatrix[j][nodeId];
        }
        System.out.println("");
        return sum;
    }
    public static void main(String[] args) {
        Random r = new Random();
        ArrayList<Task> taskArrayList = new ArrayList<>();
        Task[] tasks = new Task[6];
        for (int i = 0; i < tasks.length; i++) {
            tasks[i] = new CircleTask();
            tasks[i].getMQPS().put("attack",r.nextInt(91) + 10);
            taskArrayList.add(tasks[i]);
        }
        ArrayList<Role> roleArrayList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Role role = new Role();
            role.getCapabilities().put("attack",r.nextInt(91) + 10);
            roleArrayList.add(role);
        }
        GenerationAlgorithm generationAlgorithm = new GenerationAlgorithm();
        ArrayList<PII> ga = generationAlgorithm.ga(taskArrayList, roleArrayList);
        for (int i = 0; i < ga.size(); i++) {
            System.out.println(ga.get(i).first);
            System.out.println(ga.get(i).second);
            System.out.println();
        }
    }
}

class PII{
    int first;
    int second;

    public PII(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }
}