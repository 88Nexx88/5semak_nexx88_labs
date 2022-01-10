import csv

def csv_reader(name):
    file_in = open(name, 'r')
    lines = file_in.readlines()
    arr = [list(map(str, lines[i].replace('\n', '').split(';'))) for i in range(len(lines))]
    file_in.close()
    start_sost = arr[0][0]
    fin_sost = arr[0][1]
    strings = []
    for i in arr[0][2:]:
        if i != '':
            strings.append(i+' ')
    perehods = {}
    for i in arr[1:]:
        for j in range(len(i)):
            if i[j] == '':
                i[j] = ' '
        perehods[(i[0],i[1])] = (i[2],i[3],i[4])
    return start_sost,fin_sost,strings,perehods,arr

def csv_write(arr,file_out):
    outfile = open(file_out, 'w', newline='')
    outfile.write("".join(arr))
    outfile.close()

def Machine_of_Turing(str1,cur_sost,perehods,pointer):
    sym = str1[pointer]
    x = (cur_sost, sym)
    if x in perehods:
        y = perehods[x]
        str1 = list(str1)
        str1[pointer] = y[1]
        if y[2] == "R":
            pointer += 1
        elif y[2] == "L":
            pointer -= 1
        cur_sost = y[0]
    return cur_sost,str1,pointer

def final(cur_sost,fin_sost):
    if cur_sost in fin_sost:
        return True
    else:
        return False


start_sost,fin_sost,strings,perehods,arr = csv_reader('lab9_in.csv')
s = []
for string in strings:
    cur_sost = start_sost
    pointer = 0
    if string == ' ':
        print('Начальная строка пуста')
    else:
        print(f'source: { string }')
        while not final(cur_sost,fin_sost):
            cur_sost,string,pointer = Machine_of_Turing(string,cur_sost,perehods,pointer)
        s.append(string)
        print(f'answer: { "".join(string) }')
for i in range(len(s)):
    arr[0][i+2] = "".join(s[i])[:len(s[i])-1]
file_out = 'lab9_out.csv'
csv_write(string,file_out)