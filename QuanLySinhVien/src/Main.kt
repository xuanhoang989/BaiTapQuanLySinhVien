import java.util.Scanner

// 1. Khai báo data class Student theo đúng yêu cầu bài toán
data class Student(
    val studentId: String,
    val fullName: String,
    val age: Int,
    val major: String,
    val gpa: Double
) {
    fun displayInfo() {
        println("%-10s | %-22s | %-5d | %-20s | %-5.2f".format(studentId, fullName, age, major, gpa))
    }
}

class StudentManager {
    private val studentList = mutableListOf<Student>()

    init {
        // 5 Sinh viên mẫu riêng biệt (Mỗi sinh viên cần thay đổi lại thông tin nếu thích)
        studentList.add(Student("SV001", "Đinh Xuân Hoàng", 20, "Công nghệ thông tin", 8.6))
        studentList.add(Student("SV002", "Nguyễn Văn Hưng", 21, "Công nghệ thông tin", 7.8))
        studentList.add(Student("SV003", "Trần Tiến Đạt", 19, "Kỹ thuật phần mềm", 4.5))
        studentList.add(Student("SV004", "Lê Thị Mai", 22, "Hệ thống thông tin", 8.2))
        studentList.add(Student("SV005", "Phạm Quốc Bảo", 20, "Công nghệ thông tin", 6.4))
    }

    // In tiêu đề bảng
    private fun printHeader() {
        println("-------------------------------------------------------------------------")
        println("%-10s | %-22s | %-5s | %-20s | %-5s".format("ID", "Full Name", "Age", "Major", "GPA"))
        println("-------------------------------------------------------------------------")
    }

    // 1. Thêm sinh viên mới
    fun addStudent(scanner: Scanner) {
        println("\n--- THÊM SINH VIÊN MỚI ---")
        print("Nhập Student ID: ")
        val id = scanner.nextLine().trim()

        if (studentList.any { it.studentId.equalsIgnoreCase(id) }) {
            println("❌ Lỗi: Mã sinh viên đã tồn tại!")
            return
        }

        print("Nhập Full Name: ")
        val name = scanner.nextLine().trim()

        print("Nhập Age: ")
        val age = scanner.nextLine().toIntOrNull() ?: 0

        print("Nhập Major (Ngành học): ")
        val major = scanner.nextLine().trim()

        print("Nhập GPA (0.0 - 10.0): ")
        val gpa = scanner.nextLine().toDoubleOrNull() ?: 0.0

        studentList.add(Student(id, name, age, major, gpa))
        println("✅ Thêm sinh viên thành công!")
    }

    // 2. Hiển thị tất cả sinh viên
    fun displayAll() {
        if (studentList.isEmpty()) {
            println("Danh sách sinh viên trống!")
            return
        }
        println("\n--- DANH SÁCH TẤT CẢ SINH VIÊN ---")
        printHeader()
        studentList.forEach { it.displayInfo() }
        println("-------------------------------------------------------------------------")
    }

    // 3. Tìm kiếm sinh viên (Menu con nâng cao)
    fun searchMenu(scanner: Scanner) {
        println("\n--- TÌM KIẾM SINH VIÊN ---")
        println("1. Tìm theo một phần tên")
        println("2. Tìm tất cả sinh viên thuộc một ngành")
        println("3. Tìm sinh viên theo khoảng GPA (7.0 -> 8.5)")
        println("4. Tìm sinh viên lớn tuổi nhất")
        print("Chọn tính năng tìm kiếm: ")

        when (scanner.nextLine().trim()) {
            "1" -> {
                print("Nhập tên (hoặc một phần tên): ")
                val keyword = scanner.nextLine().trim()
                val result = studentList.filter { it.fullName.contains(keyword, ignoreCase = true) }
                showResultList(result)
            }
            "2" -> {
                print("Nhập ngành học cần tìm: ")
                val majorKeyword = scanner.nextLine().trim()
                val result = studentList.filter { it.major.contains(majorKeyword, ignoreCase = true) }
                showResultList(result)
            }
            "3" -> {
                val result = studentList.filter { it.gpa in 7.0..8.5 }
                showResultList(result)
            }
            "4" -> {
                val maxAge = studentList.maxOfOrNull { it.age }
                if (maxAge != null) {
                    val result = studentList.filter { it.age == maxAge }
                    println("\nSinh viên lớn tuổi nhất ($maxAge tuổi):")
                    showResultList(result)
                }
            }
            else -> println("Lựa chọn không hợp lệ!")
        }
    }

    // 4. Tính toán và Thống kê GPA
    fun calculateAndStatMenu(scanner: Scanner) {
        println("\n--- THỐNG KÊ VÀ TÍNH TOÁN ---")
        if (studentList.isEmpty()) {
            println("Danh sách trống!")
            return
        }

        // GPA trung bình toàn bộ
        val avgGpa = studentList.map { it.gpa }.average()
        println("• GPA trung bình toàn trường: %.2f".format(avgGpa))

        // Đếm GPA >= 8.0
        val countHigh = studentList.count { it.gpa >= 8.0 }
        println("• Số sinh viên có GPA >= 8.0: $countHigh")

        // Đếm GPA < 5.0
        val countLow = studentList.count { it.gpa < 5.0 }
        println("• Số sinh viên có GPA < 5.0: $countLow")

        // GPA trung bình theo ngành được giao
        print("• Nhập ngành muốn tính GPA trung bình: ")
        val targetMajor = scanner.nextLine().trim()
        val majorStudents = studentList.filter { it.major.contains(targetMajor, ignoreCase = true) }
        if (majorStudents.isNotEmpty()) {
            val avgMajorGpa = majorStudents.map { it.gpa }.average()
            println("  => GPA trung bình ngành '$targetMajor': %.2f".format(avgMajorGpa))
        } else {
            println("  => Không tìm thấy sinh viên thuộc ngành '$targetMajor'")
        }
    }

    // 5. Tìm sinh viên GPA cao nhất & Top 3
    fun findHighestAndTop3() {
        if (studentList.isEmpty()) {
            println("Danh sách trống!")
            return
        }

        val maxGpa = studentList.maxOfOrNull { it.gpa }
        println("\n--- SINH VIÊN CÓ GPA CAO NHẤT ($maxGpa) ---")
        val highestStudents = studentList.filter { it.gpa == maxGpa }
        showResultList(highestStudents)

        println("\n--- TOP 3 SINH VIÊN CÓ GPA CAO NHẤT ---")
        val top3 = studentList.sortedByDescending { it.gpa }.take(3)
        showResultList(top3)
    }

    // Sắp xếp sinh viên theo các tiêu chí
    fun sortMenu(scanner: Scanner) {
        println("\n--- SẮP XẾP SINH VIÊN ---")
        println("1. Sắp xếp theo GPA giảm dần")
        println("2. Sắp xếp theo tuổi")
        println("3. Sắp xếp theo tên")
        print("Chọn tiêu chí sắp xếp: ")

        when (scanner.nextLine().trim()) {
            "1" -> showResultList(studentList.sortedByDescending { it.gpa })
            "2" -> showResultList(studentList.sortedBy { it.age })
            "3" -> showResultList(studentList.sortedBy { it.fullName.split(" ").last() })
            else -> println("Lựa chọn không hợp lệ!")
        }
    }

    // 6. Xóa sinh viên
    fun removeStudent(scanner: Scanner) {
        print("\nNhập Student ID cần xóa: ")
        val id = scanner.nextLine().trim()
        val removed = studentList.removeIf { it.studentId.equalsIgnoreCase(id) }
        if (removed) {
            println("✅ Đã xóa sinh viên có ID $id thành công!")
        } else {
            println("❌ Không tìm thấy sinh viên có ID $id!")
        }
    }

    private fun showResultList(list: List<Student>) {
        if (list.isEmpty()) {
            println("Không tìm thấy sinh viên phù hợp!")
            return
        }
        printHeader()
        list.forEach { it.displayInfo() }
        println("-------------------------------------------------------------------------")
    }

    private fun String.equalsIgnoreCase(other: String): Boolean = this.equals(other, ignoreCase = true)
}

fun main() {
    val scanner = Scanner(System.`in`)
    val manager = StudentManager()

    while (true) {
        println("\n========== STUDENT MANAGEMENT ==========")
        println("1. Add student")
        println("2. Display all students")
        println("3. Search student (Theo tên, ngành, tuổi, khoảng GPA)")
        println("4. Calculate average GPA & Statistics")
        println("5. Find student with highest GPA & Top 3")
        println("6. Sort students (GPA giảm dần, Tuổi, Tên)")
        println("7. Remove student")
        println("0. Exit")
        println("================------------------------")
        print("Choose: ")

        when (scanner.nextLine().trim()) {
            "1" -> manager.addStudent(scanner)
            "2" -> manager.displayAll()
            "3" -> manager.searchMenu(scanner)
            "4" -> manager.calculateAndStatMenu(scanner)
            "5" -> manager.findHighestAndTop3()
            "6" -> manager.sortMenu(scanner)
            "7" -> manager.removeStudent(scanner)
            "0" -> {
                println("Đã thoát chương trình. Cảm ơn bạn!")
                break
            }
            else -> println("❌ Lựa chọn không hợp lệ, vui lòng chọn lại!")
        }
    }
}