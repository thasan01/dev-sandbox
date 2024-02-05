#include <iostream>

class Base {
	public:
	virtual void f() const {
		std::cout << "A::f()" << std::endl;
	}

	void g() const {
		std::cout << "A::g()" << std::endl;
		f();
	}

};

class Derived : public Base {
	public:
	virtual void f() const {
		std::cout << "Derived::f()" << std::endl;
	}
};

class Other {
public:
	virtual void f() const {
		std::cout << "Other::f()" << std::endl;
	}
};

template<typename T>
void genericTask(const T& a) {
	static_assert(std::is_base_of<Base, T>::value, "T must be derived from Base");
	a.f();
}

int main(int argc, char* argv[]) {
	Base clsA = Base();
	Derived clsB = Derived();
	Other clsC = Other();

	genericTask(clsB);
	genericTask(clsC);

}