package process;

import app.MainApp;
import columnvbox.*;
import pcb.PCB;

import static res.IOManager.addBlockedPCB;

/**
 * 管理进程
 * 按优先级调度管理进程池
 */

public class ProcessManager {
    private static int numOfReadyProcesses; // 就绪进程数
    private static PCB readyPCBHead; // 就绪PCB头指针
    private static PCB readyPCBBack; // 就绪PCB尾指针
    private static PCB backlogPCBHead; // 后备PCB头指针
    private static PCB backlogPCBBack; // 后备PCB尾指针
    private static int numOfProcesses; // 总进程数
    private static int numOfPCBs; // PCB数
    public static Process runningProcess;

    public ProcessManager() {
        readyPCBHead = null;
        readyPCBBack = null;
        backlogPCBHead = null;
        backlogPCBBack = null;
        numOfProcesses = 0;
        numOfPCBs = 0;
        numOfReadyProcesses = 0;
        runningProcess = null;
    }

    /**
     * 添加就绪队列PCB
     * @param PCB
     */
    public static void addReadyPCB(PCB PCB) {
        if (readyPCBHead == null) {
            readyPCBHead = PCB;
            readyPCBBack = PCB;
            readyPCBHead.setBack_PCB(readyPCBBack);
            readyPCBBack.setFront_PCB(readyPCBHead);
            return;
        }
        readyPCBBack.setBack_PCB(PCB);
        readyPCBHead.setFront_PCB(PCB);
        PCB.setFront_PCB(readyPCBBack);
        PCB.setBack_PCB(readyPCBHead);
        readyPCBBack = PCB;
        updateReadyPCB();
    }

    /**
     * 删除就绪队列PCB
     * @param PCB
     */
    public static void delReadyPCB(PCB PCB) {
        if (PCB.getFront_PCB() == PCB.getBack_PCB()) {
            readyPCBHead = null;
            readyPCBBack = null;
            return;
        }
        PCB front = PCB.getFront_PCB();
        PCB back = PCB.getBack_PCB();
        front.setBack_PCB(back);
        back.setFront_PCB(front);
        if (PCB == readyPCBHead) {
            readyPCBHead = back;
        }
    }

    /**
     * 从就绪队列取出头进程运行
     */
    private static void run() {
        if (readyPCBHead != null){
            runningProcess = readyPCBHead.getProcess(); // 进入CPU运行
            delReadyPCB(readyPCBHead); // 要运行的进程退出就绪队列
            updateRunningQueue();
            runningProcess.run();
        }
    }

    /**
     * 进程用于发送阻塞信号
     * 进程移入阻塞队列
     */
    static void blockSignal(Process process) {
        ListViewItem item = MainApp.get_2().getListViewTop();
        _2 _2 = MainApp.get_2();
        _2.removeListViewTopItem(item);
        _2.refreshTop();
        updateIOQueue(item);
        runningProcess = null;

        PCB PCB = process.getPCB();
        addBlockedPCB(PCB);
        run();
    }

    /**
     * 进程结束信号
     * 立即刷新一次调用
     */
    static void endRunningSignal() {
        MainApp.endRunningSignal();
    }

    public static void endIOSignal() {
        ListViewItem item = MainApp.get_2().getListViewBottom();
        _2 _2 = MainApp.get_2();
        _2.removeListViewBottomItem(item);
        _2.refreshTop();
        runningProcess = null;
    }

    /**
     * 删除后备队列PCB
     * @param PCB
     */
    public static void delBacklogPCB(PCB PCB) {
        if (PCB.getFront_PCB() == PCB.getBack_PCB()) {
            backlogPCBHead = null;
            backlogPCBBack = null;
            return;
        }
        PCB front = PCB.getFront_PCB();
        PCB back = PCB.getBack_PCB();
        front.setBack_PCB(back);
        back.setFront_PCB(front);
        if (PCB == backlogPCBHead) {
            backlogPCBHead = back;
        }
    }

    /**
     * 增加后备队列PCB
     * @param PCB
     */
    public static void addBacklogPCB(PCB PCB) {
        if (backlogPCBHead == null) {
            backlogPCBHead = PCB;
            backlogPCBBack = PCB;
            backlogPCBHead.setBack_PCB(backlogPCBBack);
            backlogPCBBack.setFront_PCB(backlogPCBHead);
            return;
        }
        backlogPCBBack.setBack_PCB(PCB);
        backlogPCBHead.setFront_PCB(PCB);
        PCB.setFront_PCB(backlogPCBBack);
        PCB.setBack_PCB(backlogPCBHead);
        backlogPCBBack = PCB;
    }

    private static void updateReadyPCB() {
        if (readyPCBHead == null || readyPCBHead == readyPCBBack) {
            // 链表为空或只有一个节点，无需排序
            return;
        }

        boolean swapped = true;
        PCB currentPCB;

        while (swapped){
            swapped = false;
            currentPCB = readyPCBHead;

            // 降序排序
            if (currentPCB.getPriority() < currentPCB.getBack_PCB().getPriority()) {
                // 头节点发生交换
                if (currentPCB == readyPCBHead) {
                    readyPCBHead = currentPCB.getBack_PCB();
                }
                // 尾节点发生交换
                if (currentPCB.getBack_PCB() == readyPCBBack) {
                    readyPCBBack = currentPCB;
                }
                // 交换节点
                swap(currentPCB,currentPCB.getBack_PCB());
            }
        }

    }

    static void swap(PCB front, PCB back) {
        PCB a = front.getFront_PCB();
        PCB b= front;
        PCB c = back;
        PCB d = back.getBack_PCB();

        a.setBack_PCB(c);
        c.setFront_PCB(a);
        c.setBack_PCB(b);
        b.setFront_PCB(c);
        b.setBack_PCB(d);
        d.setFront_PCB(b);
    }

    /**
     * 进入后备队列，刷新后备队列并显示
     * @param listViewItem
     */
    static void updateBacklogQueue(ListViewItem listViewItem) {
        addBacklogPCB(listViewItem.getPCB());
        listViewItem.getButton().setText("运行");
        _1 _1 = MainApp.get_1();
        listViewItem.getButton().setOnAction(event -> {
            _1.removeListViewItem(listViewItem);
            _1.refresh();
            delBacklogPCB(listViewItem.getPCB());
            try {
                updateReadyQueue(listViewItem);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        _1.addListViewItem(listViewItem);
        System.out.println("updateBacklogQueue");
    }

    /**
     * 刷新就绪队列并显示，并执行一次调度
     */
    public static void updateReadyQueue(ListViewItem listViewItem) throws InterruptedException {
        addReadyPCB(listViewItem.getPCB()); // 进入就绪队列，同时刷新队列和排序
        listViewItem.getButton().setText("挂起");
        listViewItem.getButton().setOnAction(null);
        _3 _3 = MainApp.get_3();
        listViewItem.getButton().setOnAction(event -> {
            _3.removeListViewTopItem(listViewItem);
            _3.refreshTop();
            delReadyPCB(listViewItem.getPCB());
            updateSuspendQueue(listViewItem);
        });

        _3.addListViewTopItem(listViewItem);
        MainApp.schedulingOnce();
        System.out.println("updateReadyQueue");
    }

    public static void updateReadyQueue(ListViewItem listViewItem, int i) throws InterruptedException {
        addReadyPCB(listViewItem.getPCB()); // 进入就绪队列，同时刷新队列和排序
        listViewItem.getButton().setText("挂起");
        listViewItem.getButton().setOnAction(null);
        _3 _3 = MainApp.get_3();
        listViewItem.getButton().setOnAction(event -> {
            _3.removeListViewTopItem(listViewItem);
            _3.refreshTop();
            delReadyPCB(listViewItem.getPCB());
            updateSuspendQueue(listViewItem);
        });

        _3.addListViewTopItem(listViewItem);
        if (i ==0) {
            MainApp.schedulingOnce();
        }
        System.out.println("updateReadyQueue");
    }


    /**
     * 刷新挂起队列并显示
     */
    static void updateSuspendQueue(ListViewItem listViewItem) {
        listViewItem.getButton().setText("解挂");
        listViewItem.getButton().setOnAction(null);
        _3 _3 = MainApp.get_3();
        listViewItem.getButton().setOnAction(event -> {
            _3.removeListViewBottomItem(listViewItem);
            _3.refreshBottom();
            try {
                updateReadyQueue(listViewItem);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        _3.addListViewBottomItem(listViewItem);
        System.out.println("updateSuspendQueue");
    }

    /**
     * 刷新完成队列并显示
     */
    public static void updateCompletionQueue(ListViewItem listViewItem) {
        listViewItem.getButton().setOnAction(null);
        _4 _4 = MainApp.get_4();
        listViewItem.getButton().setOnAction(event -> {
            _4.removeListViewItem(listViewItem);
            _4.refresh();
            listViewItem.init();
            updateBacklogQueue(listViewItem);
        });

        _4.addListViewItem(listViewItem);
        System.out.println("updateCompletionQueue");
    }

    static void updateRunningQueue() {
        _3 _3 = MainApp.get_3();
        ListViewItem listViewItem = _3.removeFirstListViewTopItem();
        listViewItem.getButton().setText("挂起  ");
        listViewItem.getButton().setOnAction(null);
        _2 _2 = MainApp.get_2();
        listViewItem.getButton().setOnAction(event -> {
            listViewItem.getPCB().getProcess().interrupt(); // 中断
            runningProcess = null;
            ListViewItem item = MainApp.get_2().getListViewTop();
            _2.removeListViewTopItem(listViewItem);
            _2.refreshTop();
            updateSuspendQueue(listViewItem);
        });

        _2.addListViewTopItem(listViewItem);
        System.out.println("updateRunningQueue");
    }

    static void updateIOQueue(ListViewItem listViewItem) {
        listViewItem.getButton().setText("挂起    ");
        listViewItem.getButton().setOnAction(null);
        _2 _2 = MainApp.get_2();
        listViewItem.getButton().setOnAction(event -> {
            _2.removeListViewBottomItem(listViewItem);
            _2.refreshBottom();
            updateSuspendQueue(listViewItem);
        });

        _2.addListViewBottomItem(listViewItem);
        System.out.println("updateIOQueue");
    }

    /**
     * 创建进程，进入后备队列，刷新后备队列并显示
     *
     * @param name
     * @param presetPriority
     * @param presetRuntime
     * @param IO
     * @param presetIOTime
     */
    public void createProcess(String name, int presetPriority, long presetRuntime, int IO, long presetIOTime) {
        Process process = null;
        if (IO == 0) {
            process = new Process(name, presetPriority, presetRuntime);
        } else {
            process = new Process(name, presetPriority, presetRuntime, presetIOTime);
        }
        numOfProcesses++;
        numOfPCBs = numOfProcesses;
        process.setId(numOfProcesses);
        PCB PCB = new PCB(numOfPCBs, process);
        process.setPCB(PCB);
        ListViewItem listViewItem = new ListViewItem(numOfProcesses, name, presetPriority, presetRuntime, presetIOTime);
        listViewItem.setPCB(PCB);
        updateBacklogQueue(listViewItem);
    }

    /**
     * 执行优先级调度
     * 高优先级进程优先加入CPU运行，中断低优先级进程占用CPU
     */
    public void priorityScheduling() {
        if (readyPCBHead == null) {
            System.out.println("priorityScheduling():readyPCBHead null");
            return;
        }

        // CPU空闲
        if (runningProcess == null) {
            run();
            return;
        }
        // CPU占用进程优先于就绪队列头进程优先级比较
        if (readyPCBHead.getProcess().getPresetPriority() > runningProcess.getPresetPriority()) {
            runningProcess.interrupt(); // 中断
            ListViewItem item = MainApp.get_2().getListViewTop();
            _2 _2 = MainApp.get_2();
            _2.removeListViewTopItem(item);
            _2.refreshTop();
            addReadyPCB(runningProcess.getPCB()); // 中断进程进入就绪队列
            _3 _3 = MainApp.get_3();
            _3.addListViewTopItem(item);
            run();
            System.out.println("interrupted");
        }
        // 占用CPU进程优先级最高，继续运行
    }

}
