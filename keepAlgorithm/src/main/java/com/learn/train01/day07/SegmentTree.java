package com.learn.train01.day07;

import java.util.Arrays;

/**
 * 线段树  使用范围  L 。。。 R 上的  最大值、最小值，累加和 刷房子问题 位带代表颜色
 * f 父 由 s子简单加工得到， 不需要知道子的状况
 */
public class SegmentTree {
    public static class MySegmentTree {
        int MAXN;
        int[] nArr;
        int[] lazy;
        int[] sum;
        int[] change;
        boolean[] update;

        public MySegmentTree(int[] arr) {
            MAXN = arr.length + 1;
            nArr = new int[MAXN];
            lazy = new int[MAXN << 2];
            sum = new int[MAXN << 2];
            change = new int[MAXN << 2];
            update = new boolean[MAXN << 2];
            for (int i = 0; i < arr.length; i++) {
                nArr[i + 1] = arr[i];
            }

//            build(1, MAXN, 1);
        }

        public void build(int l, int r, int rt) {
            if (nArr == null || nArr.length == 0) {
                return;
            }
            if (r == l){
                // bug 5 给 sum[rt] 赋值，下标搞错了
                sum[rt] = nArr[l];
//                sum[l] = nArr[rt];
                return;
            }
            int mid = (r + l ) / 2; // 1,2,3,4,5,6,7,8
            // bug 6 build 递归是 rt 标识 l。。。r 段在 sum 树中的位置
            build(l, mid, rt * 2);
            build(mid + 1, r, rt * 2+1);
            pushUp(rt);
        }

        // 子信息更新完成，汇总到父
        private void pushUp(int rt) {
            sum[rt] = sum[rt * 2] + sum[rt * 2 + 1];
        }

        // 父的懒信息位置，向下传递
        private void pushDown(int rt, int ln, int rn) {

            if (update[rt]){
                sum[rt * 2] = change[rt] * ln;
                change[rt * 2] = change[rt];
                update[rt * 2] = true;
                lazy[rt * 2] = 0;
                sum[rt * 2 + 1] = change[rt] * rn;
                change[rt * 2 + 1] = change[rt];
                update[rt * 2 + 1] = true;
                lazy[rt * 2 + 1] = 0;
                update[rt] = false;
                change[rt] = 0;
            }



            // bug 3 赋值 出错
            // 隐藏规则，当前树是满二叉树，左右孩子都存在
            if (lazy[rt] != 0) {
                lazy[rt * 2] += lazy[rt];
                sum[rt * 2] += lazy[rt] * ln;

                // bug 4 加号没了
                lazy[rt * 2 + 1] += lazy[rt];
                sum[rt * 2 + 1] += lazy[rt] * rn;
                lazy[rt] = 0;
            }
        }

        public void add(int L, int R, int C,
                        int l, int r,
                        int rt) {
            if (L <= l && R >= r){
                lazy[rt] += C;
                sum[rt] += (r-l+1) * C;
                return;
            }


            int mid = (r + l) / 2;

            pushDown(rt,mid - l+1,r-mid);
            // 任务接触到左边
            if (L <= mid){
                add(L,R ,C ,l ,mid,rt *2);
            }

            // 任务接触遇到右边
            if (mid < R){
                add(L,R,C ,mid+1,r, rt * 2 + 1);
            }

            pushUp(rt);
        }

        public void update( int L, int R, int C,
                            int l, int r,
                            int rt) {

            if (L <= l && R >= r){
                change[rt] = C;
                update[rt] = true;
                sum[rt] = (r-l+1) * C;
                lazy[rt] = 0;
                return;
            }

            // bug 1 忘记加括号
            int mid = (r + l) / 2;

            pushDown(rt,mid - l+1,r-mid);
            // 任务接触到左边
            if (L <= mid){
                update(L,R ,C ,l ,mid,rt *2);
            }

            // 任务接触遇到右边
            if (mid < R){
                update(L,R,C ,mid+1,r, rt * 2 + 1);
            }

            pushUp(rt);
        }

        public int query(int L, int R, int l, int r, int rt) {
            // bug 2 base case 范围 出错
            if (L <= l && R >= r){
                return sum[rt];
            }
            int result = 0;

            int mid = (r+l)/2;
            pushDown(rt, mid - l + 1, r - mid);
            if (L <= mid){
                result +=query(L,R,l,mid,rt * 2);
            }
            // bug 7 R接触到右边
            if (mid < R){
                result +=query(L,R,mid+1,r,rt * 2 + 1);
            }
            return result;
        }
    }

    public static class Right {
        public int[] arr;

        public Right(int[] origin) {
            arr = new int[origin.length + 1];
            for (int i = 0; i < origin.length; i++) {
                arr[i + 1] = origin[i];
            }
        }

        public void update(int L, int R, int C) {
            for (int i = L; i <= R; i++) {
                arr[i] = C;
            }
        }

        public void add(int L, int R, int C) {
            for (int i = L; i <= R; i++) {
                arr[i] += C;
            }
        }

        public long query(int L, int R) {
            long ans = 0;
            for (int i = L; i <= R; i++) {
                ans += arr[i];
            }
            return ans;
        }



    }

    public static int[] genarateRandomArray(int len, int max) {
        int size = (int) (Math.random() * len) + 1;
        int[] origin = new int[size];
        for (int i = 0; i < size; i++) {
            origin[i] = (int) (Math.random() * max) - (int) (Math.random() * max);
        }
        return origin;
    }

    public static boolean test() {
        int len = 100;
        int max = 1000;
        int testTimes = 5000;
        int addOrUpdateTimes = 1000;
        int queryTimes = 500;
        for (int i = 0; i < testTimes; i++) {
            int[] origin = genarateRandomArray(len, max);
            MySegmentTree seg = new MySegmentTree(origin);
            int S = 1;
            int N = origin.length;
            int root = 1;
            seg.build(S, N, root);
            Right rig = new Right(origin);
            for (int j = 0; j < addOrUpdateTimes; j++) {
                int num1 = (int) (Math.random() * N) + 1;
                int num2 = (int) (Math.random() * N) + 1;
                int L = Math.min(num1, num2);
                int R = Math.max(num1, num2);
                int C = (int) (Math.random() * max) - (int) (Math.random() * max);
                if (Math.random() < 0.5) {
                    seg.add(L, R, C, S, N, root);
                    rig.add(L, R, C);
                } else {
                    seg.update(L, R, C, S, N, root);
                    rig.update(L, R, C);
                }
            }
            for (int k = 0; k < queryTimes; k++) {
                int num1 = (int) (Math.random() * N) + 1;
                int num2 = (int) (Math.random() * N) + 1;
                int L = Math.min(num1, num2);
                int R = Math.max(num1, num2);
                long ans1 = seg.query(L, R, S, N, root);
                long ans2 = rig.query(L, R);
                if (ans1 != ans2) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int[] origin = { 2, 1, 1, 2, 3, 4, 5 };
        MySegmentTree seg = new MySegmentTree(origin);
        int S = 1; // 整个区间的开始位置，规定从1开始，不从0开始 -> 固定
        int N = origin.length; // 整个区间的结束位置，规定能到N，不是N-1 -> 固定
        int root = 1; // 整棵树的头节点位置，规定是1，不是0 -> 固定
        int L = 2; // 操作区间的开始位置 -> 可变
        int R = 5; // 操作区间的结束位置 -> 可变
        int C = 4; // 要加的数字或者要更新的数字 -> 可变
        // 区间生成，必须在[S,N]整个范围上build
        seg.build(S, N, root);
        // 区间修改，可以改变L、R和C的值，其他值不可改变
        seg.add(L, R, C, S, N, root);
        // 区间更新，可以改变L、R和C的值，其他值不可改变
        seg.update(L, R, C, S, N, root);
        // 区间查询，可以改变L和R的值，其他值不可改变
        long sum = seg.query(L, R, S, N, root);
        System.out.println(sum);

        System.out.println("对数器测试开始...");
        System.out.println("测试结果 : " + (test() ? "通过" : "未通过"));

    }


}
