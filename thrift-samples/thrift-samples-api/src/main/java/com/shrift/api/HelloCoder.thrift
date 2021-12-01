namespace java com.shrift.api
service Hello{
    string helloString(1:string para)
    i32 helloInt(1:i32 para)
    bool helloBoolean(1:bool para)
    void helloVoid()
    string helloNull()
    Person getPerson(1:Person para)
}

struct Person {
  1: i32    id,
  2: string firstName,
  3: string lastName,
  4: string email,
  5: list<Phone> phones
}

struct Phone {
  1: i32    id,
  2: string number
}