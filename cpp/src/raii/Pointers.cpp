#include <iostream> 

class VirtualClass {
	public:
		VirtualClass() {
			std::cout << "INVOKED A's Constructor" << std::endl;
		}

		~VirtualClass() {
			std::cout << "INVOKED A's Destructor" << std::endl;
		}

};

void f1() {
	VirtualClass c1 = VirtualClass();
}

VirtualClass f2()
{
	VirtualClass c1 = VirtualClass();
	return c1;
}

VirtualClass f3()
{
	return VirtualClass();
}

//warning C4172: returning address of local variable or temporary: c1
VirtualClass& f4()
{
	VirtualClass c1;
	return c1;
}

VirtualClass* f5()
{
	return new VirtualClass();
}

std::shared_ptr<VirtualClass> f6()
{
	return std::shared_ptr<VirtualClass>(new VirtualClass());
}

std::shared_ptr<VirtualClass> f7()
{
	return std::shared_ptr<VirtualClass>(new VirtualClass());
}

std::unique_ptr<VirtualClass> f8()
{
	return std::unique_ptr<VirtualClass>(new VirtualClass());
}

int main(int argc, char* argv[]) {
	std::cout << "ENTERING main app" << std::endl << std::endl;

	std::cout << "ENTERING f1()" << std::endl;
	f1();
	std::cout << "RETURNING FROM f1()" << std::endl << std::endl;

	std::cout << "ENTERING f2()" << std::endl;
	f2();
	std::cout << "RETURNING FROM f2()" << std::endl << std::endl;

	std::cout << "ENTERING f3()" << std::endl;
	f3();
	std::cout << "RETURNING FROM f3()" << std::endl << std::endl;

	std::cout << "ENTERING f4()" << std::endl;
	f4();
	std::cout << "RETURNING FROM f4()" << std::endl << std::endl;

	std::cout << "ENTERING f5()" << std::endl;
	f5();
	std::cout << "RETURNING FROM f5()" << std::endl << std::endl;

	std::cout << "ENTERING f6 Block" << std::endl;
	{
		std::shared_ptr<VirtualClass> g_c1;
		std::cout << "ENTERING f6()" << std::endl;
		auto c1=f6();
		g_c1 = c1;
		std::cout << "RETURNING FROM f6()" << std::endl;
	}
	std::cout << "RETURNING FROM f6 Block" << std::endl << std::endl;




	std::cout << "EXITING main app" << std::endl << std::endl;
}