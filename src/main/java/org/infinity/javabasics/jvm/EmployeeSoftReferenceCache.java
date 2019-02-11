package org.infinity.javabasics.jvm;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Hashtable;

public class EmployeeSoftReferenceCache {

    static private EmployeeSoftReferenceCache cache; // 一个 Cache实例

    private Hashtable<String, EmployeeRef> employeeRefs; // 用于Cache内容的存储
    private ReferenceQueue<Employee>       q; // 垃圾Reference的队列

    // 继承SoftReference，使得每一个实例都具有可识别的标识，并且该标识与其在HashMap内的key相同。
    private class EmployeeRef extends SoftReference<Employee> {

        private String _key = "";

        public EmployeeRef(Employee em, ReferenceQueue<Employee> q) {
            super(em, q);
            _key = em.getId();
        }
    }

    // 构建一个缓存器实例
    private EmployeeSoftReferenceCache() {
        employeeRefs = new Hashtable<String, EmployeeRef>();
        q = new ReferenceQueue<Employee>();
    }

    // 取得缓存器实例
    public static synchronized EmployeeSoftReferenceCache getInstance() {
        if (cache == null) {
            cache = new EmployeeSoftReferenceCache();
        }
        return cache;

    }

    // 以软引用的方式对一个 Employee对象的实例进行引用并保存该引用
    private void cacheEmployee(Employee em) {

        cleanCache(); // 清除垃圾引用
        EmployeeRef ref = new EmployeeRef(em, q);
        employeeRefs.put(em.getId(), ref);
    }

    // 依据所指定的 ID号，重新获取相应 Employee对象的实例
    public Employee getEmployee(String ID) {

        Employee em = null;

        // 缓存中是否有该 Employee实例的软引用，如果有，从软引用中取得。
        if (employeeRefs.containsKey(ID)) {
            EmployeeRef ref = (EmployeeRef) employeeRefs.get(ID);
            em = (Employee) ref.get();
        }

        // 如果没有软引用，或者从软引用中得到的实例是 null，重新构建一个实例，

        // 并保存对这个新建实例的软引用
        if (em == null) {
            em = new Employee(ID);
            System.out.println("Retrieve From EmployeeInfoCenter. ID=" + ID);
            this.cacheEmployee(em);
        }

        return em;
    }

// 清除那些所软引用的 Employee对象已经被回收的EmployeeRef对象

    private void cleanCache() {
        EmployeeRef ref = null;
        while ((ref = (EmployeeRef) q.poll()) != null) {
            employeeRefs.remove(ref._key);

        }
    }
}