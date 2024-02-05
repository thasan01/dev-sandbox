#include <iostream>
#include <vector>
using namespace std;

void print( const vector<int>& vec) {
	for (auto iter = vec.begin(); iter != vec.end(); std::advance(iter,1)) {
		cout << *iter << ",";
	}
	cout << endl;
	 
}

int main(int argc, char* argv[]) {
	vector<int> v;
	v.push_back(1);
	v.push_back(2);
	v.push_back(3);

	print(v);

	swap(v[0], v[2]);
	print(v);


}