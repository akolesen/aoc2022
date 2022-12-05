use std::io::{self, BufRead};
use bitset_core::BitSet;

fn main() {
    let dict = (b'a' ..= b'z').chain(b'A' ..= b'Z').map(char::from).collect::<Vec<_>>();

    let mut sum1 = 0;

    let mut bs2 = [0u8; 16];
    bs2.bit_init(true);
    let mut sum2 = 0;

    for (n, iter) in io::stdin().lock().lines().enumerate() {
        let line = iter.unwrap();

        let mut first_half = [0u8; 16];
        let mut second_half = [0u8; 16];

        for (i, c) in line.chars().enumerate() {
            let c = c as usize;
            if i < line.len()/2 {
                first_half.bit_set(c);
            }
            else {
                second_half.bit_set(c);
            }
        }

        let mut bs = first_half;
        bs.bit_or(&second_half);

        first_half.bit_and(&second_half);
        for (n, c) in dict.iter().enumerate() {
            if first_half.bit_test(*c as usize) {
                sum1 += n + 1;
            }
        }

        bs2.bit_and(&bs);
        if n % 3 == 2 {
            for (n, c) in dict.iter().enumerate() {
                if bs2.bit_test(*c as usize) {
                    sum2 += n + 1;
                }
            }
            bs2.bit_init(true);
        }
    }
    println!("{}", sum1);
    println!("{}", sum2);
}
